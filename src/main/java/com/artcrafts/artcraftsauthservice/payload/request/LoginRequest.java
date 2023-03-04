package com.artcrafts.artcraftsauthservice.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Details about the login request")
public class LoginRequest {
    @NotBlank
    @ApiModelProperty(notes = "The user's username(s)")
    private String username;

    @NotBlank
    @ApiModelProperty(notes = "The user's password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
