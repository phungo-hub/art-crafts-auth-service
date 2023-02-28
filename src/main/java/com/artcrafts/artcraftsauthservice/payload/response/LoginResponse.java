package com.artcrafts.artcraftsauthservice.payload.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "Details about the login response")
public class LoginResponse {
    @NotBlank
    @ApiModelProperty(notes = "The user's username(s)")
    private String message;

    @Nullable
    @ApiModelProperty(notes = "The returned authToken")
    private String token;

    public LoginResponse() {
        super();
    }

    public LoginResponse(@NotBlank String message, String token) {
        super();
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
