package org.example.service;

import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setCreatedAt(LocalDateTime.now());

        when(userMapper.insert(any(User.class))).thenReturn(1);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userMapper).insert(user);
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testUser");

        when(userMapper.selectById(1)).thenReturn(user);

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(
            new User(),
            new User()
        );

        when(userMapper.selectAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("updatedUser");

        when(userMapper.update(any(User.class))).thenReturn(1);

        boolean result = userService.updateUser(user);

        assertTrue(result);
        verify(userMapper).update(user);
    }

    @Test
    void deleteUser() {
        when(userMapper.delete(1)).thenReturn(1);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userMapper).delete(1);
    }

    @Test
    void getUserByUsername() {
        User user = new User();
        user.setUsername("testUser");

        when(userMapper.selectByUsername("testUser")).thenReturn(user);

        User result = userService.getUserByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void getUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userMapper.selectByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
} 