package com.artcrafts.artcraftsauthservice.service;

import com.artcrafts.artcraftsauthservice.entity.Role;
import com.artcrafts.artcraftsauthservice.entity.RoleName;
import com.artcrafts.artcraftsauthservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
