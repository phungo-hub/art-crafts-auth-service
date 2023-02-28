package com.artcrafts.artcraftsauthservice.controller;


import com.artcrafts.artcraftsauthservice.payload.request.LoginRequest;
import com.artcrafts.artcraftsauthservice.payload.response.LoginResponse;
import com.artcrafts.artcraftsauthservice.repository.UserRepository;
import com.artcrafts.artcraftsauthservice.security.JwtTokenProvider;
import com.artcrafts.artcraftsauthservice.service.SecurityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    SecurityService securityService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    @ApiOperation(value = "Login into Admin system",
            notes = "Provide username and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Gọi hàm authenticate để xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Gọi hàm tạo Token
            String token = tokenProvider.generateToken(authentication);
            return new ResponseEntity<>(new LoginResponse("Đăng nhập thành công!", token), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new LoginResponse("Đăng nhập thất bại!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth-validate")
    @ApiOperation(value = "Authenticate and validate token for other services",
            notes = "Take in an authToken as parameter from other services")
    public Boolean validateAuthenticate(@ApiParam(value = "Token value from other services")
                    @RequestHeader(value = "Authorization") String token) {
        if (securityService.isAuthenticated() && securityService.isValidToken(token))
            return true;
        return false;
    }

    @PostMapping("/return-role")
    @ApiOperation(value = "Return roles for authorization to other services",
            notes = "Take in an authToken as parameter from other services ")
    public List<String> returnRoles(@ApiParam(value = "Token value from other services")
            @RequestHeader(value = "Authorization") String authToken) {
        if (authToken.startsWith("Bearer"))
            authToken = authToken.substring(7);
        String username = tokenProvider.getUsernameFromJWT(authToken);
        List<String> roles = userRepository.findRolesByUsername(username);
        return roles;
    }
}
