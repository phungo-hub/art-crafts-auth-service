package com.artcrafts.artcraftsauthservice.service;


import com.artcrafts.artcraftsauthservice.dto.UserDto;
import com.artcrafts.artcraftsauthservice.entity.User;

import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    Iterable<UserDto> findUsersByNameContaining(String user_name);
    User save(UserDto userDto);

    Iterable<UserDto> findAll();

    void remove(Long id);

    Optional<UserDto> findById(Long id);
}
