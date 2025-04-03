package com.example.auction_web.utils.Quataz;

import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.ScheduleLog.SessionLog;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.SessionLogRepository;
import com.example.auction_web.utils.Job.AuctionSessionEndTimeJob;
import com.example.auction_web.utils.Job.AuctionSessionStartJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final Scheduler scheduler;
    private final AuctionSessionRepository auctionSessionRepository;
    private final SessionLogRepository sessionLogRepository;
    private final AuctionHistoryRepository auctionHistoryRepository;

    public void scheduleAuctionSessionStart(String auctionSessionId, LocalDateTime startTime) {
        JobKey jobKey = new JobKey("auctionSessionStartJob-" + auctionSessionId, "auctionSessionGroup");
        TriggerKey triggerKey = new TriggerKey("auctionSessionStartTrigger-" + auctionSessionId, "auctionSessionGroup");

        try {
            // Kiểm tra nếu Job đã tồn tại
            if (scheduler.checkExists(jobKey)) {
                System.out.println("Job đã tồn tại: " + jobKey);
                return;
            }

            JobDetail jobDetail = JobBuilder.newJob(AuctionSessionStartJob.class)
                    .withIdentity(jobKey)
                    .usingJobData("auctionSessionId", auctionSessionId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            // Kiểm tra và lưu SessionLog nếu chưa tồn tại
            if (sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.UPCOMING.toString()) == null) {
                SessionLog sessionLog = new SessionLog();
                sessionLog.setAuctionSessionId(auctionSessionId);
                sessionLog.setScheduledTime(startTime);
                sessionLog.setCurrentStatus(AUCTION_STATUS.UPCOMING.toString());
                sessionLog.setStatus(SessionLog.SessionLogStatus.SCHEDULED);
                sessionLogRepository.save(sessionLog);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void scheduleAuctionSessionEnd(String auctionSessionId, LocalDateTime endTime) {
        JobKey jobKey = new JobKey("auctionSessionEndJob-" + auctionSessionId, "auctionSessionGroup");
        TriggerKey triggerKey = new TriggerKey("auctionSessionEndTrigger-" + auctionSessionId, "auctionSessionGroup");

        try {
            // Kiểm tra nếu Job đã tồn tại
            if (scheduler.checkExists(jobKey)) {
                System.out.println("Job đã tồn tại: " + jobKey);
                return;
            }

            JobDetail jobDetail = JobBuilder.newJob(AuctionSessionEndTimeJob.class)
                    .withIdentity(jobKey)
                    .usingJobData("auctionSessionId", auctionSessionId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startAt(Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            // Kiểm tra và lưu SessionLog nếu chưa tồn tại
            if (sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.ONGOING.toString()) == null) {
                SessionLog sessionLog = new SessionLog();
                sessionLog.setAuctionSessionId(auctionSessionId);
                sessionLog.setScheduledTime(endTime);
                sessionLog.setCurrentStatus(AUCTION_STATUS.ONGOING.toString());
                sessionLog.setStatus(SessionLog.SessionLogStatus.SCHEDULED);
                sessionLogRepository.save(sessionLog);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void reschedulePendingSessions() {
        List<SessionLog> sessionLogs = sessionLogRepository.findByStatus(SessionLog.SessionLogStatus.SCHEDULED);
        sessionLogs.forEach(this::rescheduleSession);
    }

    private void rescheduleSession(SessionLog sessionLog) {
        LocalDateTime sessionTime = sessionLog.getScheduledTime();
        String auctionSessionId = sessionLog.getAuctionSessionId();
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId).orElse(null);

        if (auctionSession == null) {
            // Handle the case when the auction session is not found
            sessionLog.setStatus(SessionLog.SessionLogStatus.FAILED);
            sessionLogRepository.save(sessionLog);
            return;
        }

        List<AuctionHistory> auctionHistory = auctionHistoryRepository.findAuctionHistorysByAuctionSession_AuctionSessionId(auctionSessionId);
        LocalDateTime startTime = auctionSession.getStartTime();
        LocalDateTime endTime = auctionSession.getEndTime();

        if (sessionTime.isAfter(LocalDateTime.now()) && sessionLog.getStatus() == SessionLog.SessionLogStatus.SCHEDULED) {
            if (Objects.equals(sessionLog.getCurrentStatus(), AUCTION_STATUS.UPCOMING.toString())) {
                scheduleAuctionSessionStart(auctionSessionId, startTime);
            } else if (Objects.equals(sessionLog.getCurrentStatus(), AUCTION_STATUS.ONGOING.toString())) {
                scheduleAuctionSessionEnd(auctionSessionId, endTime);
            }
        } else if (sessionTime.isBefore(LocalDateTime.now()) && sessionLog.getStatus() == SessionLog.SessionLogStatus.SCHEDULED) {
            sessionLog.setStatus(SessionLog.SessionLogStatus.SENT);
            sessionLogRepository.save(sessionLog);

            if (Objects.equals(sessionLog.getCurrentStatus(), AUCTION_STATUS.UPCOMING.toString())) {
                auctionSession.setStatus(AUCTION_STATUS.ONGOING.toString());
            } else if (Objects.equals(sessionLog.getCurrentStatus(), AUCTION_STATUS.ONGOING.toString())) {
                if (auctionHistory.isEmpty()) {
                    auctionSession.setStatus(AUCTION_STATUS.AUCTION_FAILED.toString());
                } else {
                    auctionSession.setStatus(AUCTION_STATUS.AUCTION_SUCCESS.toString());
                }
            }
            auctionSessionRepository.save(auctionSession);
        } else {
            sessionLog.setStatus(SessionLog.SessionLogStatus.FAILED);
            sessionLogRepository.save(sessionLog);
        }
    }

    public void updateAuctionSession(String auctionSessionId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            SessionLog timeStart = sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.UPCOMING.toString());
            SessionLog timeEnd = sessionLogRepository.findSessionLogByAuctionSessionIdAndCurrentStatus(auctionSessionId, AUCTION_STATUS.ONGOING.toString());

            if (!newStartTime.isEqual(timeStart.getScheduledTime())) {
                JobKey startJobKey = new JobKey("auctionSessionStartJob-" + auctionSessionId, "auctionSessionGroup");
                if (scheduler.checkExists(startJobKey)) {
                    scheduler.deleteJob(startJobKey);
                }
                scheduleAuctionSessionStart(auctionSessionId, newStartTime);
                timeStart.setScheduledTime(newStartTime);
                timeStart.setStatus(SessionLog.SessionLogStatus.SCHEDULED);
                sessionLogRepository.save(timeStart);
            }

            if (!newEndTime.isEqual(timeEnd.getScheduledTime())) {
                JobKey endJobKey = new JobKey("auctionSessionEndJob-" + auctionSessionId, "auctionSessionGroup");
                if (scheduler.checkExists(endJobKey)) {
                    scheduler.deleteJob(endJobKey);
                }
                scheduleAuctionSessionEnd(auctionSessionId, newEndTime);
                timeEnd.setScheduledTime(newEndTime);
                timeEnd.setStatus(SessionLog.SessionLogStatus.SCHEDULED);
                sessionLogRepository.save(timeEnd);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update auction session schedule", e);
        }
    }

    public void deleteScheduleAuctionSession(String auctionSessionId) {
        try {
            JobKey startJobKey = new JobKey("auctionSessionStartJob-" + auctionSessionId, "auctionSessionGroup");
            JobKey endJobKey = new JobKey("auctionSessionEndJob-" + auctionSessionId, "auctionSessionGroup");

            if (scheduler.checkExists(startJobKey)) {
                scheduler.deleteJob(startJobKey);
            }
            if (scheduler.checkExists(endJobKey)) {
                scheduler.deleteJob(endJobKey);
            }

            sessionLogRepository.deleteAllByAuctionSessionId(auctionSessionId);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete auction session schedule", e);
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        reschedulePendingSessions();
    }
}
