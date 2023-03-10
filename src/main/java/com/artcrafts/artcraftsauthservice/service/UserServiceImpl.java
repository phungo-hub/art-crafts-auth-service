package com.artcrafts.artcraftsauthservice.service;

import com.artcrafts.artcraftsauthservice.dto.UserDto;
import com.artcrafts.artcraftsauthservice.entity.Role;
import com.artcrafts.artcraftsauthservice.entity.RoleName;
import com.artcrafts.artcraftsauthservice.entity.User;
import com.artcrafts.artcraftsauthservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Iterable<UserDto> findUsersByNameContaining(String user_name) {
        Iterable<User> entities = userRepository.findUsersByNameContaining(user_name);
        return StreamSupport.stream(entities.spliterator(), true)
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public User save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Set<String> strRoles = userDto.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);
                    break;

                case "super_admin":
                    Role superAdminRole = roleService.findByName(RoleName.ROLE_SUPER_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(superAdminRole);
                    break;

                default:
                    Role admin = roleService.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(admin);
            }
        });

        user.setRoles(roles);
        if (!userDto.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
            user.setPassword(hashedPassword);
        }
        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<UserDto> findAll() {
        Iterable<User> entities = userRepository.findAll();

        return StreamSupport.stream(entities.spliterator(), true)
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        User entity = userRepository.findById(id).orElse(null);
        return Optional.ofNullable(modelMapper.map(entity, UserDto.class));
    }
}
