package com.codedecode.userinfo.controller;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        // Manual validation
        if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (userDTO.getUserPassword() == null || userDTO.getUserPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (userDTO.getAddress() == null || userDTO.getAddress().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (userDTO.getCity() == null || userDTO.getCity().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        UserDTO userAdded = userService.addUser(userDTO);
        return new ResponseEntity<>(userAdded, HttpStatus.CREATED);
    }

    @GetMapping("/fetchUserById/{userId}")
    public ResponseEntity<UserDTO> fetchUserDetailsById(@PathVariable Integer userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return userService.fetchUserDetailsById(userId);
    }
}
