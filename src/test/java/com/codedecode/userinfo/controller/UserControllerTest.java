package com.codedecode.userinfo.controller;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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

        verify(userService).addUser(any(UserDTO.class));
    }

    @Test
    void addUser_WithEmptyUserName_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setUserName("");  // Invalid username

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithNullUserName_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setUserName(null);  // Null username

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithWhitespaceUserName_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setUserName("   ");  // Whitespace only username

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithEmptyPassword_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setUserPassword("");  // Invalid password

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithNullPassword_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setUserPassword(null);  // Null password

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithEmptyAddress_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setAddress("");  // Invalid address

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithNullAddress_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setAddress(null);  // Null address

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithEmptyCity_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setCity("");  // Invalid city

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_WithNullCity_ShouldReturnBadRequest() throws Exception {
        testUserDTO.setCity(null);  // Null city

        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetchUserDetailsById_ShouldReturnUser() throws Exception {
        when(userService.fetchUserDetailsById(1))
                .thenReturn(ResponseEntity.ok(testUserDTO));

        mockMvc.perform(get("/user/fetchUserById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserDTO.getUserId()))
                .andExpect(jsonPath("$.userName").value(testUserDTO.getUserName()))
                .andExpect(jsonPath("$.address").value(testUserDTO.getAddress()))
                .andExpect(jsonPath("$.city").value(testUserDTO.getCity()));

        verify(userService).fetchUserDetailsById(1);
    }

    @Test
    void fetchUserDetailsById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/user/fetchUserById/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetchUserDetailsById_WithZeroId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/user/fetchUserById/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fetchUserDetailsById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        when(userService.fetchUserDetailsById(999))
                .thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/user/fetchUserById/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).fetchUserDetailsById(999);
    }
}
