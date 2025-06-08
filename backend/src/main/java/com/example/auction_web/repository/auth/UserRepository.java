package com.example.auction_web.repository.auth;

import com.example.auction_web.dto.response.auth.UserInfoBase;
import com.example.auction_web.entity.auth.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    User findUserByEmail(String email);
    User findUserByUserId(String userId);

    @Query("SELECT new com.example.auction_web.dto.response.auth.UserInfoBase(u.userId, u.username, u.name, u.avatar) " +
            "FROM User u WHERE u.userId = :userId")
    UserInfoBase findUserInfoBaseByUserId(@Param("userId") String userId);

}
