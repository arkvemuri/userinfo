package com.codedecode.userinfo.service;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.entity.User;
import com.codedecode.userinfo.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private UserDTO testUserDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test UserDTO
        testUserDTO = new UserDTO();
        testUserDTO.setUserId(1);
        testUserDTO.setUserName("Test User");
        testUserDTO.setUserPassword("password123");
        testUserDTO.setAddress("123 Test St");
        testUserDTO.setCity("Test City");

        // Setup test User entity
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("Test User");
        testUser.setUserPassword("password123");
        testUser.setAddress("123 Test St");
        testUser.setCity("Test City");
    }

    @Test
    void addUser_ShouldSaveAndReturnDTO() {
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.addUser(testUserDTO);

        assertNotNull(result);
        assertEquals(testUserDTO.getUserId(), result.getUserId());
        assertEquals(testUserDTO.getUserName(), result.getUserName());
        assertEquals(testUserDTO.getAddress(), result.getAddress());
        assertEquals(testUserDTO.getCity(), result.getCity());
    }

    @Test
    void fetchUserDetailsById_WhenUserExists_ShouldReturnUser() {
        when(userRepo.findById(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<UserDTO> response = userService.fetchUserDetailsById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUserDTO.getUserId(), response.getBody().getUserId());
        assertEquals(testUserDTO.getUserName(), response.getBody().getUserName());
        assertEquals(testUserDTO.getAddress(), response.getBody().getAddress());
        assertEquals(testUserDTO.getCity(), response.getBody().getCity());
    }

    @Test
    void fetchUserDetailsById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        when(userRepo.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userService.fetchUserDetailsById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
