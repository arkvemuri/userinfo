package com.codedecode.userinfo.service;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.entity.User;
import com.codedecode.userinfo.mapper.UserMapper;
import com.codedecode.userinfo.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.userMapper = UserMapper.INSTANCE;
    }

    public UserDTO addUser(UserDTO userDTO) {
        Assert.notNull(userDTO, "UserDTO must not be null");
        User user = userMapper.mapUserDTOToUser(userDTO);
        User savedUser = userRepo.save(user);
        return userMapper.mapUserToUserDTO(savedUser);
    }

    public ResponseEntity<UserDTO> fetchUserDetailsById(Integer userId) {
        Assert.notNull(userId, "User ID must not be null");
        return userRepo.findById(userId)
                .map(user -> ResponseEntity.ok(userMapper.mapUserToUserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}
