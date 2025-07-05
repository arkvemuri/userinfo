package com.codedecode.userinfo.mapper;

import com.codedecode.userinfo.dto.UserDTO;
import com.codedecode.userinfo.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void testMapUserToUserDTO() {
        // Given
        User user = new User();
        user.setUserId(1);
        user.setUserName("John Doe");
        user.setUserPassword("password123");
        user.setAddress("123 Main St");
        user.setCity("New York");

        // When
        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUserId(), userDTO.getUserId());
        assertEquals(user.getUserName(), userDTO.getUserName());
        assertEquals(user.getUserPassword(), userDTO.getUserPassword());
        assertEquals(user.getAddress(), userDTO.getAddress());
        assertEquals(user.getCity(), userDTO.getCity());
    }

    @Test
    void testMapUserDTOToUser() {
        // Given
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(2);
        userDTO.setUserName("Jane Smith");
        userDTO.setUserPassword("securepass");
        userDTO.setAddress("456 Oak Ave");
        userDTO.setCity("Los Angeles");

        // When
        User user = userMapper.mapUserDTOToUser(userDTO);

        // Then
        assertNotNull(user);
        assertEquals(userDTO.getUserId(), user.getUserId());
        assertEquals(userDTO.getUserName(), user.getUserName());
        assertEquals(userDTO.getUserPassword(), user.getUserPassword());
        assertEquals(userDTO.getAddress(), user.getAddress());
        assertEquals(userDTO.getCity(), user.getCity());
    }

    @Test
    void testMapUserToUserDTO_WithNullValues() {
        // Given
        User user = new User();
        user.setUserId(1);
        user.setUserName("John Doe");
        // Other fields are null

        // When
        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getUserId(), userDTO.getUserId());
        assertEquals(user.getUserName(), userDTO.getUserName());
        assertNull(userDTO.getUserPassword());
        assertNull(userDTO.getAddress());
        assertNull(userDTO.getCity());
    }

    @Test
    void testMapperInstance() {
        assertNotNull(UserMapper.INSTANCE);
        assertSame(UserMapper.INSTANCE, UserMapper.INSTANCE);
    }

    @Test
    void testMapNullUser() {
        UserDTO result = userMapper.mapUserToUserDTO(null);
        assertNull(result);
    }

    @Test
    void testMapNullUserDTO() {
        User result = userMapper.mapUserDTOToUser(null);
        assertNull(result);
    }
}