package com.artcrafts.artcraftsauthservice.repository;

import com.artcrafts.artcraftsauthservice.entity.Role;
import com.artcrafts.artcraftsauthservice.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
