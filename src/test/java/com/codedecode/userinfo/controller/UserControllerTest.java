package com.codedecode.userinfo.controller;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

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

    private static Stream<Arguments> invalidUserScenarios() {
        return Stream.of(
            // Invalid username scenarios
            Arguments.of(createInvalidUser("", "password123", "123 Test St", "Test City")),
            Arguments.of(createInvalidUser(null, "password123", "123 Test St", "Test City")),
            Arguments.of(createInvalidUser("   ", "password123", "123 Test St", "Test City")),
            // Invalid password scenarios
            Arguments.of(createInvalidUser("testUser", "", "123 Test St", "Test City")),
            Arguments.of(createInvalidUser("testUser", null, "123 Test St", "Test City")),
            Arguments.of(createInvalidUser("testUser", "   ", "123 Test St", "Test City")),
            // Invalid address scenarios
            Arguments.of(createInvalidUser("testUser", "password123", "", "Test City")),
            Arguments.of(createInvalidUser("testUser", "password123", null, "Test City")),
            Arguments.of(createInvalidUser("testUser", "password123", "   ", "Test City")),
            // Invalid city scenarios
            Arguments.of(createInvalidUser("testUser", "password123", "123 Test St", "")),
            Arguments.of(createInvalidUser("testUser", "password123", "123 Test St", null)),
            Arguments.of(createInvalidUser("testUser", "password123", "123 Test St", "   "))
        );
    }

    private static UserDTO createInvalidUser(String username, String password, String address, String city) {
        UserDTO dto = new UserDTO();
        dto.setUserId(1);
        dto.setUserName(username);
        dto.setUserPassword(password);
        dto.setAddress(address);
        dto.setCity(city);
        return dto;
    }

    @ParameterizedTest
    @MethodSource("invalidUserScenarios")
    void addUser_WithInvalidData_ShouldReturnBadRequest(UserDTO invalidUser) throws Exception {
        mockMvc.perform(post("/user/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
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

    private static Stream<Arguments> invalidUserIdScenarios() {
        return Stream.of(
            Arguments.of(-1, "isBadRequest"), // Invalid negative ID
            Arguments.of(0, "isBadRequest"),  // Invalid zero ID
            Arguments.of(999, "isNotFound")   // Non-existent ID
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUserIdScenarios")
    void fetchUserDetailsById_WithInvalidId_ShouldReturnExpectedStatus(int userId, String expectedStatus) throws Exception {
        // Setup mock for non-existent ID scenario
        if (userId == 999) {
            when(userService.fetchUserDetailsById(userId))
                    .thenReturn(ResponseEntity.notFound().build());
        }

        var requestBuilder = mockMvc.perform(get("/user/fetchUserById/" + userId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert based on expected status
        switch (expectedStatus) {
            case "isBadRequest":
                requestBuilder.andExpect(status().isBadRequest());
                break;
            case "isNotFound":
                requestBuilder.andExpect(status().isNotFound());
                verify(userService).fetchUserDetailsById(userId);
                break;
            default:
                throw new IllegalArgumentException("Unexpected status value: " + expectedStatus);
        }
    }
}
