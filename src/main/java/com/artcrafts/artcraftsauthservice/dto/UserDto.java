package com.artcrafts.artcraftsauthservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Set<String> roles;
    public void addRole(String role) {
        roles.add(role);
    }
}