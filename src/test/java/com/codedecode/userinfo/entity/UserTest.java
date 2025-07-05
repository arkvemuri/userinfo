package com.codedecode.userinfo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
        assertEquals(0, user.getUserId());
        assertNull(user.getUserName());
        assertNull(user.getUserPassword());
        assertNull(user.getAddress());
        assertNull(user.getCity());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1, "John Doe", "password123", "123 Main St", "New York");
        
        assertEquals(1, user.getUserId());
        assertEquals("John Doe", user.getUserName());
        assertEquals("password123", user.getUserPassword());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("New York", user.getCity());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        
        user.setUserId(2);
        user.setUserName("Jane Smith");
        user.setUserPassword("securepass");
        user.setAddress("456 Oak Ave");
        user.setCity("Los Angeles");
        
        assertEquals(2, user.getUserId());
        assertEquals("Jane Smith", user.getUserName());
        assertEquals("securepass", user.getUserPassword());
        assertEquals("456 Oak Ave", user.getAddress());
        assertEquals("Los Angeles", user.getCity());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User(1, "John Doe", "password123", "123 Main St", "New York");
        User user2 = new User(1, "John Doe", "password123", "123 Main St", "New York");
        User user3 = new User(2, "Jane Smith", "password456", "456 Oak Ave", "Los Angeles");
        
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = new User(1, "John Doe", "password123", "123 Main St", "New York");
        String toString = user.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("123 Main St"));
        assertTrue(toString.contains("New York"));
    }
}