package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.User;
import co.edu.javeriana.easymarket.usersservice.repository.UserRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Common test user for all tests
        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFullname("Test User");
        testUser.setRol("user");
        testUser.setDeleted(false);
    }

    /**
     * Test retrieving users with no filters
     * Should return all non-deleted users
     */
    @Test
    void getUsersWithNoFilters() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findUsersByFilters(null, null, null, null, false)).thenReturn(userList);
        
        // Act
        List<User> result = userService.getUsers(null, null, null, null, null);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        verify(userRepository).findUsersByFilters(null, null, null, null, false);
    }
    
    /**
     * Test retrieving users with specific filters
     */
    @Test
    void getUsersWithFilters() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findUsersByFilters("Test User", "testuser", "test@example.com", "user", false))
            .thenReturn(userList);
        
        // Act
        List<User> result = userService.getUsers("Test User", "testuser", "test@example.com", "user", false);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
    }
    
    /**
     * Test successful user retrieval by ID
     */
    @Test
    void getUserById_Success() throws OperationException {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act
        User result = userService.getUser("1");
        
        // Assert
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }
    
    /**
     * Test getting a non-existent user
     * Should throw OperationException with 404 status
     */
    @Test
    void getUserById_NotFound() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.getUser("999");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
    
    /**
     * Test getting a deleted user
     * Should throw OperationException
     */
    @Test
    void getUserById_IsDeleted() {
        // Arrange
        testUser.setDeleted(true);
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.getUser("1");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User account is deleted", exception.getMessage());
        
        // Reset testUser state for subsequent tests
        testUser.setDeleted(false);
    }
    
    /**
     * Test successful user creation
     */
    @Test
    void createUser_Success() throws OperationException {
        // Arrange
        User newUser = new User();
        newUser.setId("2");
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        
        // Act
        User result = userService.createUser(newUser);
        
        // Assert
        assertEquals("2", result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertFalse(result.getDeleted());
    }
    
    /**
     * Test creating user with existing ID
     * Should throw OperationException
     */
    @Test
    void createUser_AlreadyExists() {
        // Arrange
        User newUser = new User();
        newUser.setId("1");  // Same ID as testUser
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals(400, exception.getCode());
        assertEquals("User already exists", exception.getMessage());
    }
    
    /**
     * Test creating user with email already in use
     */
    @Test
    void createUser_EmailInUse() {
        // Arrange
        User newUser = new User();
        newUser.setId("2");
        newUser.setEmail("test@example.com");  // Same email as testUser
        
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals(400, exception.getCode());
        assertEquals("Email already in use", exception.getMessage());
    }
    
    /**
     * Test creating user with username already in use
     */
    @Test
    void createUser_UsernameInUse() {
        // Arrange
        User newUser = new User();
        newUser.setId("2");
        newUser.setEmail("new@example.com");
        newUser.setUsername("testuser");  // Same username as testUser
        
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals(400, exception.getCode());
        assertEquals("Username already in use", exception.getMessage());
    }
    
    /**
     * Test server error during user creation
     */
    @Test
    void createUser_ServerError() {
        // Arrange
        User newUser = new User();
        newUser.setId("2");
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.createUser(newUser);
        });
        
        assertEquals(500, exception.getCode());
        assertEquals("User could not be created", exception.getMessage());
    }
    
    /**
     * Test successful user update with all fields
     */
    @Test
    void updateUser_Success() throws OperationException {
        // Arrange
        User updateData = new User();
        updateData.setEmail("updated@example.com");
        updateData.setUsername("updateduser");
        updateData.setFullname("Updated User");
        updateData.setRol("admin");
        
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setUsername("updateduser");
        updatedUser.setFullname("Updated User");
        updatedUser.setRol("admin");
        updatedUser.setDeleted(false);
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("updateduser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        // Act
        User result = userService.updateUser("1", updateData);
        
        // Assert
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updateduser", result.getUsername());
        assertEquals("Updated User", result.getFullname());
        assertEquals("admin", result.getRol());
    }
    
    /**
     * Test updating a non-existent user
     */
    @Test
    void updateUser_NotFound() {
        // Arrange
        User updateData = new User();
        updateData.setEmail("updated@example.com");
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.updateUser("999", updateData);
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
    
    /**
     * Test updating user with email already in use
     */
    @Test
    void updateUser_EmailInUse() {
        // Arrange
        User otherUser = new User();
        otherUser.setId("2");
        otherUser.setEmail("other@example.com");
        
        User updateData = new User();
        updateData.setEmail("other@example.com");  // Email already in use
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.updateUser("1", updateData);
        });
        
        assertEquals(400, exception.getCode());
        assertEquals("Email already in use", exception.getMessage());
    }
    
    /**
     * Test updating user with username already in use
     */
    @Test
    void updateUser_UsernameInUse() {
        // Arrange
        User otherUser = new User();
        otherUser.setId("2");
        otherUser.setUsername("otheruser");
        
        User updateData = new User();
        updateData.setUsername("otheruser");  // Username already in use
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.updateUser("1", updateData);
        });
        
        assertEquals(400, exception.getCode());
        assertEquals("Username already in use", exception.getMessage());
    }
    
    /**
     * Test successful soft deletion of a user
     */
    @Test
    void deleteUser_Success() throws OperationException {
        // Arrange
        User deletedUser = new User();
        deletedUser.setId("1");
        deletedUser.setUsername("testuser");
        deletedUser.setEmail("test@example.com");
        deletedUser.setDeleted(true);
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(deletedUser);
        
        // Act
        User result = userService.deleteUser("1");
        
        // Assert
        assertTrue(result.getDeleted());
    }
    
    /**
     * Test deleting a non-existent user
     */
    @Test
    void deleteUser_NotFound() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.deleteUser("999");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
    
    /**
     * Test deleting an already deleted user
     */
    @Test
    void deleteUser_AlreadyDeleted() {
        // Arrange
        testUser.setDeleted(true);
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.deleteUser("1");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User account is already deleted", exception.getMessage());
        
        // Reset testUser state
        testUser.setDeleted(false);
    }
    
    /**
     * Test successful restoration of a deleted user
     */
    @Test
    void restoreUser_Success() throws OperationException {
        // Arrange
        testUser.setDeleted(true);
        
        User restoredUser = new User();
        restoredUser.setId("1");
        restoredUser.setDeleted(false);
        
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(restoredUser);
        
        // Act
        User result = userService.restoreUser("1");
        
        // Assert
        assertFalse(result.getDeleted());
        
        // Reset testUser state
        testUser.setDeleted(false);
    }
    
    /**
     * Test restoring a non-existent user
     */
    @Test
    void restoreUser_NotFound() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.restoreUser("999");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
    
    /**
     * Test restoring an already active user
     * Should not call save method
     */
    @Test
    void restoreUser_AlreadyActive() throws OperationException {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act
        User result = userService.restoreUser("1");
        
        // Assert
        assertFalse(result.getDeleted());
        verify(userRepository, never()).save(any(User.class)); // Verify save was not called
    }
    
    /**
     * Test successful permanent deletion of a user
     */
    @Test
    void deletePermanentlyUser_Success() {
        // Arrange
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            userService.deletePermanentlyUser("1");
        });
        
        verify(userRepository).delete(testUser);
    }
    
    /**
     * Test permanently deleting a non-existent user
     */
    @Test
    void deletePermanentlyUser_NotFound() {
        // Arrange
        when(userRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            userService.deletePermanentlyUser("999");
        });
        
        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
    
    /**
     * Test retrieving all admin email addresses
     */
    @Test
    void getAllAdminEmails() {
        // Arrange
        List<User> adminUsers = new ArrayList<>();
        
        User admin1 = new User();
        admin1.setEmail("admin1@example.com");
        admin1.setRol("admin");
        
        User admin2 = new User();
        admin2.setEmail("admin2@example.com");
        admin2.setRol("admin");
        
        adminUsers.add(admin1);
        adminUsers.add(admin2);
        
        when(userRepository.findUsersByFilters(null, null, null, "admin", false)).thenReturn(adminUsers);
        
        // Act
        String[] result = userService.getAllAdminEmails();
        
        // Assert
        assertEquals(2, result.length);
        assertEquals("admin1@example.com", result[0]);
        assertEquals("admin2@example.com", result[1]);
    }
}