package com.artcrafts.artcraftsauthservice.repository;

import com.artcrafts.artcraftsauthservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Iterable<User> findUsersByNameContaining(String user_name);

    @Query(nativeQuery = true,
            value = "SELECT r.name FROM users u " +
                    "JOIN users_roles ur ON u.id = ur.user_id " +
                    "JOIN roles r ON ur.role_id = r.id " +
                    "WHERE u.username = :username")
    List<String> findRolesByUsername(@Param("username") String username);
}