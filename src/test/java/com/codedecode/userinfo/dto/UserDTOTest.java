package com.codedecode.userinfo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testNoArgsConstructor() {
        UserDTO userDTO = new UserDTO();
        assertNotNull(userDTO);
        assertEquals(0, userDTO.getUserId());
        assertNull(userDTO.getUserName());
        assertNull(userDTO.getUserPassword());
        assertNull(userDTO.getAddress());
        assertNull(userDTO.getCity());
    }

    @Test
    void testAllArgsConstructor() {
        UserDTO userDTO = new UserDTO(1, "John Doe", "password123", "123 Main St", "New York");
        
        assertEquals(1, userDTO.getUserId());
        assertEquals("John Doe", userDTO.getUserName());
        assertEquals("password123", userDTO.getUserPassword());
        assertEquals("123 Main St", userDTO.getAddress());
        assertEquals("New York", userDTO.getCity());
    }

    @Test
    void testSettersAndGetters() {
        UserDTO userDTO = new UserDTO();
        
        userDTO.setUserId(2);
        userDTO.setUserName("Jane Smith");
        userDTO.setUserPassword("securepass");
        userDTO.setAddress("456 Oak Ave");
        userDTO.setCity("Los Angeles");
        
        assertEquals(2, userDTO.getUserId());
        assertEquals("Jane Smith", userDTO.getUserName());
        assertEquals("securepass", userDTO.getUserPassword());
        assertEquals("456 Oak Ave", userDTO.getAddress());
        assertEquals("Los Angeles", userDTO.getCity());
    }

    @Test
    void testEqualsAndHashCode() {
        UserDTO userDTO1 = new UserDTO(1, "John Doe", "password123", "123 Main St", "New York");
        UserDTO userDTO2 = new UserDTO(1, "John Doe", "password123", "123 Main St", "New York");
        UserDTO userDTO3 = new UserDTO(2, "Jane Smith", "password456", "456 Oak Ave", "Los Angeles");
        
        assertEquals(userDTO1, userDTO2);
        assertNotEquals(userDTO1, userDTO3);
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }

    @Test
    void testToString() {
        UserDTO userDTO = new UserDTO(1, "John Doe", "password123", "123 Main St", "New York");
        String toString = userDTO.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("123 Main St"));
        assertTrue(toString.contains("New York"));
    }
}