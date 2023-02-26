package com.artcrafts.artcraftsauthservice.service;


import com.artcrafts.artcraftsauthservice.entity.Role;
import com.artcrafts.artcraftsauthservice.entity.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(RoleName name);

}
