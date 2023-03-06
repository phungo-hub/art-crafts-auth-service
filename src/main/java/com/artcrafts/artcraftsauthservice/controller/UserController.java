package com.artcrafts.artcraftsauthservice.controller;

import com.artcrafts.artcraftsauthservice.dto.UserDto;
import com.artcrafts.artcraftsauthservice.payload.response.ResponseMessage;
import com.artcrafts.artcraftsauthservice.service.RoleService;
import com.artcrafts.artcraftsauthservice.service.SecurityService;
import com.artcrafts.artcraftsauthservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private static String imageDirectory = System.getProperty("user.dir") + "/images/";
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    SecurityService securityService;

    @GetMapping
    public ResponseEntity<?> showUsers(@RequestHeader("Authorization") final String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> NOT FOUND"), HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(new ResponseMessage("Fail -> NOT FOUND"), HttpStatus.UNAUTHORIZED);
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
    @ApiOperation(value = "update User by id")
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
    @ApiOperation(value = "delete User by id")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<UserDto> userDto = userService.findById(id);

        if (!userDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.remove(userDto.get().getId());
        return new ResponseEntity<>(new ResponseMessage("Delete success!"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "show User by id")
    public ResponseEntity<?> showUser(@PathVariable Long id, @RequestHeader("Authorization") final String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> NOT FOUND"), HttpStatus.NOT_FOUND);
        }

        Optional<UserDto> userDto = userService.findById(id);
        if (!userDto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
    @RequestMapping(value = "/uploadImage", produces = {MediaType.IMAGE_JPEG_VALUE, "application/json"})
    public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile file,
                                         @RequestParam("imageName") String name) {
        makeDirectoryIfNotExist(imageDirectory);
        Path fileNamePath = Paths.get(imageDirectory,
                name.concat(".").concat(FilenameUtils.getExtension(file.getOriginalFilename())));
        try {
            Files.write(fileNamePath, file.getBytes());
            return new ResponseEntity<>(name, HttpStatus.CREATED);
        } catch (IOException ex) {
            return new ResponseEntity<>("Image is not uploaded", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search by username")
    public ResponseEntity<?> searchByCustomerId(@RequestParam(name = "username", required = false) String username, @RequestHeader("Authorization") String authToken) {
        if (!securityService.isAuthenticated() && !securityService.isValidToken(authToken)) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> NOT FOUND"), HttpStatus.NOT_FOUND);
        }

        Iterable<UserDto> userDto;
        if (username == null || username.equals("")) {
            userDto = userService.findAll();
        } else {
            try {
                userDto = userService.findUsersByNameContaining(username);
            } catch (NumberFormatException e) {
                return new ResponseEntity<>(new ResponseMessage("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
        }

        if (userDto == null) {
            return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}