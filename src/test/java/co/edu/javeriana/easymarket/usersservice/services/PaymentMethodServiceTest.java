package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.PaymentMethod;
import co.edu.javeriana.easymarket.usersservice.repository.PaymentMethodRepository;
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
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    private PaymentMethod testPaymentMethod;

    @BeforeEach
    void setUp() {
        // Common test payment method for all tests
        testPaymentMethod = new PaymentMethod();
        testPaymentMethod.setId(1);
        testPaymentMethod.setUserId("user123");
        testPaymentMethod.setCardNumber("1234567890123456");
        testPaymentMethod.setExpiryDate("12/25");
        testPaymentMethod.setCardHolderName("Test User");
        testPaymentMethod.setEmail("test@example.com");
    }

    /**
     * Test retrieving payment methods by user ID
     */
    @Test
    void getPaymentMethodsByUserId_Success() {
        // Arrange
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(testPaymentMethod);
        when(paymentMethodRepository.findByUserId("user123")).thenReturn(paymentMethods);

        // Act
        List<PaymentMethod> result = paymentMethodService.getPaymentMethodsByUserId("user123");

        // Assert
        assertEquals(1, result.size());
        assertEquals(testPaymentMethod.getId(), result.get(0).getId());
        assertEquals(testPaymentMethod.getUserId(), result.get(0).getUserId());
    }

    /**
     * Test retrieving payment method by ID - successful case
     */
    @Test
    void getPaymentMethodById_Success() {
        // Arrange
        when(paymentMethodRepository.findById(1)).thenReturn(Optional.of(testPaymentMethod));

        // Act
        PaymentMethod result = paymentMethodService.getPaymentMethodById(1);

        // Assert
        assertEquals(testPaymentMethod.getId(), result.getId());
        assertEquals(testPaymentMethod.getUserId(), result.getUserId());
    }

    /**
     * Test retrieving payment method by ID - not found case
     */
    @Test
    void getPaymentMethodById_NotFound() {
        // Arrange
        when(paymentMethodRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.getPaymentMethodById(999);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Payment method not found", exception.getMessage());
    }

    /**
     * Test creating payment method - successful case
     */
    @Test
    void createPaymentMethod_Success() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(true);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(testPaymentMethod);

        // Act
        PaymentMethod result = paymentMethodService.createPaymentMethod(testPaymentMethod);

        // Assert
        assertEquals(testPaymentMethod.getId(), result.getId());
        assertEquals(testPaymentMethod.getUserId(), result.getUserId());
    }

    /**
     * Test creating payment method - user not found case
     */
    @Test
    void createPaymentMethod_UserNotFound() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(false);

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.createPaymentMethod(testPaymentMethod);
        });

        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Test creating payment method - error during creation
     */
    @Test
    void createPaymentMethod_Error() {
        // Arrange
        when(userRepository.existsById("user123")).thenReturn(true);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.createPaymentMethod(testPaymentMethod);
        });

        assertEquals(400, exception.getCode());
        assertTrue(exception.getMessage().startsWith("Error creating payment method"));
    }

    /**
     * Test deleting payment method - successful case
     */
    @Test
    void deletePaymentMethod_Success() {
        // Arrange
        when(paymentMethodRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.of(testPaymentMethod));

        // Act
        assertDoesNotThrow(() -> {
            paymentMethodService.deletePaymentMethod("user123", 1);
        });

        // Assert
        verify(paymentMethodRepository).deleteById(1);
    }

    /**
     * Test deleting payment method - not found case
     */
    @Test
    void deletePaymentMethod_NotFound() {
        // Arrange
        when(paymentMethodRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.deletePaymentMethod("user123", 1);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Payment method or user not found", exception.getMessage());
    }

    /**
     * Test updating payment method - successful case
     */
    @Test
    void updatePaymentMethod_Success() {
        // Arrange
        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        updatedPaymentMethod.setId(1);
        updatedPaymentMethod.setUserId("user123");
        updatedPaymentMethod.setCardNumber("9876543210123456");
        updatedPaymentMethod.setExpiryDate("06/26");
        updatedPaymentMethod.setCardHolderName("Updated User");
        updatedPaymentMethod.setEmail("updated@example.com");
        updatedPaymentMethod.setPhone("1234567890");
        updatedPaymentMethod.setCity("New York");
        updatedPaymentMethod.setFirstLine("123 Main St");
        updatedPaymentMethod.setSecondLine("Apt 4B");
        updatedPaymentMethod.setCountry("USA");
        updatedPaymentMethod.setPostalCode("10001");
        updatedPaymentMethod.setStateName("NY");

        when(paymentMethodRepository.findById(1)).thenReturn(Optional.of(testPaymentMethod));
        when(userRepository.existsById("user123")).thenReturn(true);
        when(paymentMethodRepository.save(any(PaymentMethod.class))).thenReturn(updatedPaymentMethod);

        // Act
        PaymentMethod result = paymentMethodService.updatePaymentMethod(1, updatedPaymentMethod);

        // Assert
        assertEquals(updatedPaymentMethod.getId(), result.getId());
        assertEquals(updatedPaymentMethod.getCardNumber(), result.getCardNumber());
        assertEquals(updatedPaymentMethod.getExpiryDate(), result.getExpiryDate());
        assertEquals(updatedPaymentMethod.getCardHolderName(), result.getCardHolderName());
        assertEquals(updatedPaymentMethod.getEmail(), result.getEmail());
        assertEquals(updatedPaymentMethod.getPhone(), result.getPhone());
        assertEquals(updatedPaymentMethod.getCity(), result.getCity());
        assertEquals(updatedPaymentMethod.getFirstLine(), result.getFirstLine());
        assertEquals(updatedPaymentMethod.getSecondLine(), result.getSecondLine());
        assertEquals(updatedPaymentMethod.getCountry(), result.getCountry());
        assertEquals(updatedPaymentMethod.getPostalCode(), result.getPostalCode());
        assertEquals(updatedPaymentMethod.getStateName(), result.getStateName());
    }

    /**
     * Test updating payment method - payment method not found case
     */
    @Test
    void updatePaymentMethod_PaymentMethodNotFound() {
        // Arrange
        when(paymentMethodRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.updatePaymentMethod(999, testPaymentMethod);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Payment method not found", exception.getMessage());
    }

    /**
     * Test updating payment method - user not found case
     */
    @Test
    void updatePaymentMethod_UserNotFound() {
        // Arrange
        when(paymentMethodRepository.findById(1)).thenReturn(Optional.of(testPaymentMethod));
        when(userRepository.existsById("user123")).thenReturn(false);

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            paymentMethodService.updatePaymentMethod(1, testPaymentMethod);
        });

        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
    }
}