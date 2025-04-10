package com.example.auction_web.configuration;

import com.example.auction_web.constant.PredefinedRole;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.auth.Role;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.InspectorRepository;
import com.example.auction_web.repository.auth.RoleRepository;
import com.example.auction_web.repository.auth.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_EMAIL = "webonlineauction@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin@123A";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, InspectorRepository inspectorRepository, BalanceUserRepository balanceUserRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User adminUser = User.builder()
                        .username(ADMIN_USER_NAME)
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .enabled(true)
                        .build();

                try {
                    userRepository.save(adminUser);
                    log.warn("Admin user has been created with default password: admin, please change it");

                    // Tạo và lưu Inspector cho User Admin
                    Inspector inspector = Inspector.builder()
                            .user(adminUser)
                            .license("LICENSE123")
                            .build();
                    inspectorRepository.save(inspector);

                    BalanceUser balanceUser = BalanceUser.builder()
                            .user(adminUser)
                            .accountBalance(BigDecimal.valueOf(100_000_000_000L))
                            .delFlag(false)
                            .build();
                    balanceUserRepository.save(balanceUser);

                    log.info("Inspector has been created for the admin user.");
                } catch (Exception e){
                    log.warn("failed");
                }
            }
            log.info("Application initialization completed .....");
        };
    }
}
