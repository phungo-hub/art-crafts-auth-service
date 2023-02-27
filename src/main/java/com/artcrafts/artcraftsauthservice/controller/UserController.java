package com.artcrafts.artcraftsauthservice.controller;

import com.artcrafts.artcraftsauthservice.dto.UserDto;
import com.artcrafts.artcraftsauthservice.payload.response.ResponseMessage;
import com.artcrafts.artcraftsauthservice.service.RoleService;
import com.artcrafts.artcraftsauthservice.service.SecurityService;
import com.artcrafts.artcraftsauthservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    SecurityService securityService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> showUsers(@RequestHeader("Authorization") final String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<String>("Unauthorized error.", HttpStatus.UNAUTHORIZED);
        }

        Iterable<UserDto> users = userService.findAll();
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, @RequestHeader("Authorization") final String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<String>("Unauthorized error.", HttpStatus.UNAUTHORIZED);
        }
        if (userService.existsByUsername(userDto.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        userService.save(userDto);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        Optional<UserDto> userDto1 = userService.findById(id);

        if (!userDto1.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (userDto1.get().getName().trim().isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("The name is required"), HttpStatus.OK);
        }
        userDto1.get().setName(userDto.getName());
        userDto1.get().setAvatar(userDto.getAvatar());
        userDto1.get().setRoles(userDto.getRoles());
        userDto1.get().setPassword(userDto.getPassword());
        userDto1.get().setEmail(userDto.getEmail());
        userService.save(userDto1.get());
        return new ResponseEntity<>(new ResponseMessage("Update success!"), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<UserDto> userDto = userService.findById(id);

        if (!userDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.remove(userDto.get().getId());
        return new ResponseEntity<>(new ResponseMessage("Delete success!"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showUser(@PathVariable Long id, @RequestHeader("Authorization") final String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<String>("Unauthorized error.", HttpStatus.UNAUTHORIZED);
        }

        Optional<UserDto> userDto = userService.findById(id);
        if (!userDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}