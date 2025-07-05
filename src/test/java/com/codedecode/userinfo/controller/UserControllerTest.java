package com.codedecode.userinfo.controller;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setUserId(1);
        testUserDTO.setUserName("Test User");
        testUserDTO.setUserPassword("password123");
        testUserDTO.setAddress("123 Test St");
        testUserDTO.setCity("Test City");
    }

    @Test
    void addUser_ShouldCreateUser() throws Exception {
        when(userService.addUser(any(UserDTO.class))).thenReturn(testUserDTO);

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(testUserDTO.getUserId()))
                .andExpect(jsonPath("$.userName").value(testUserDTO.getUserName()))
                .andExpect(jsonPath("$.address").value(testUserDTO.getAddress()))
                .andExpect(jsonPath("$.city").value(testUserDTO.getCity()));
    }

    @Test
    void fetchUserDetailsById_ShouldReturnUser() throws Exception {
        when(userService.fetchUserDetailsById(1))
                .thenReturn(new ResponseEntity<>(testUserDTO, HttpStatus.OK));

        mockMvc.perform(get("/user/fetchUserById/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserDTO.getUserId()))
                .andExpect(jsonPath("$.userName").value(testUserDTO.getUserName()))
                .andExpect(jsonPath("$.address").value(testUserDTO.getAddress()))
                .andExpect(jsonPath("$.city").value(testUserDTO.getCity()));
    }

    @Test
    void fetchUserDetailsById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(userService.fetchUserDetailsById(999))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/user/fetchUserById/{userId}", 999)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
