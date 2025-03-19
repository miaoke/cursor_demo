package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setCreatedAt(System.currentTimeMillis());
    }

    @Test
    void createUser() {
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        
        ResponseEntity<User> response = userController.createUser(new User());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getUserById() {
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        
        ResponseEntity<User> response = userController.getUserById(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }
    
    @Test
    void getUserById_NotFound() {
        when(userService.getUserById(anyLong())).thenReturn(null);
        
        ResponseEntity<User> response = userController.getUserById(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);
        
        ResponseEntity<List<User>> response = userController.getAllUsers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser() {
        when(userService.updateUser(any(User.class))).thenReturn(testUser);
        
        ResponseEntity<User> response = userController.updateUser(1L, new User());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void deleteUser() {
        doNothing().when(userService).deleteUser(anyLong());
        
        ResponseEntity<Void> response = userController.deleteUser(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }
    
    @Test
    void getUserByUsername() {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        
        ResponseEntity<User> response = userController.getUserByUsername("testuser");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserByUsername("testuser");
    }
    
    @Test
    void getUserByEmail() {
        when(userService.getUserByEmail(anyString())).thenReturn(testUser);
        
        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }
} 