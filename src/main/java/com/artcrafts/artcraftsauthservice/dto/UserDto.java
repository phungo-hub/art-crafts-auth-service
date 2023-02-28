package com.artcrafts.artcraftsauthservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    @ApiModelProperty(notes = "The user's name")
    private String name;
    @ApiModelProperty(notes = "The required username")
    private String username;
    @ApiModelProperty(notes = "The user's password")
    private String password;
    @ApiModelProperty(notes = "The user's email")
    private String email;
    @ApiModelProperty(notes = "The user's avatar")
    private String avatar;
    @ApiModelProperty(notes = "The user's role(s)")
    private Set<String> roles;
    public void addRole(String role) {
        roles.add(role);
    }
}