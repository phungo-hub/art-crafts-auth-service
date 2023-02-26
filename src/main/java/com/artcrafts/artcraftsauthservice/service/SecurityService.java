package com.artcrafts.artcraftsauthservice.service;

public interface SecurityService {
    boolean isAuthenticated();
    boolean isValidToken(String token);
}
