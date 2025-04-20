package com.example.auction_web.repository.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.enums.NotificationType;

@Repository
public interface NotificateRepository extends JpaRepository<Notification, String> {
    List<Notification> findByReceiverAndDelFlagFalseOrderByCreatedAtDesc(User receiver);
    List<Notification> findByReceiverAndIsReadFalseAndDelFlagFalseOrderByCreatedAtDesc(User receiver);
    long countByReceiverAndIsReadFalseAndDelFlagFalse(User receiver);
    Optional<Notification> findFirstBySenderAndReceiverAndTypeAndReferenceId(
        User sender,
        User receiver,
        NotificationType type,
        String referenceId
    );
}
