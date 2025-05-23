package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.Address;
import co.edu.javeriana.easymarket.usersservice.repository.AddressRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address testAddress;

    @BeforeEach
    void setUp() {
        // Common test address for all tests
        testAddress = new Address();
        testAddress.setId(1);
        testAddress.setUserId("user123");
        testAddress.setName("Home");
        testAddress.setAddress("123 Main St");
        testAddress.setDetails("Apartment 4B");
        testAddress.setLatitude(BigDecimal.valueOf(40.7128));
        testAddress.setLongitude(BigDecimal.valueOf(-74.0060));
    }

    /**
     * Test retrieving addresses by user ID
     */
    @Test
    void getAddresses_Success() {
        // Arrange
        List<Address> addresses = new ArrayList<>();
        addresses.add(testAddress);
        when(addressRepository.findByUserId("user123")).thenReturn(addresses);

        // Act
        List<Address> result = addressService.getAddresses("user123");

        // Assert
        assertEquals(1, result.size());
        assertEquals(testAddress.getId(), result.get(0).getId());
        assertEquals(testAddress.getUserId(), result.get(0).getUserId());
    }

    /**
     * Test creating address - successful case
     */
    @Test
    void createAddress_Success() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // Act
        Address result = addressService.createAddress(testAddress);

        // Assert
        assertEquals(testAddress.getId(), result.getId());
        assertEquals(testAddress.getUserId(), result.getUserId());
        assertEquals(testAddress.getName(), result.getName());
    }

    /**
     * Test creating address - error case
     */
    @Test
    void createAddress_Error() {
        // Arrange
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            addressService.createAddress(testAddress);
        });

        assertEquals(400, exception.getCode());
        assertEquals("User id not exists", exception.getMessage());
    }

    /**
     * Test retrieving address by ID and user ID - successful case
     */
    @Test
    void getAddressById_Success() {
        // Arrange
        when(addressRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.of(testAddress));

        // Act
        Address result = addressService.getAddressById("user123", 1);

        // Assert
        assertEquals(testAddress.getId(), result.getId());
        assertEquals(testAddress.getUserId(), result.getUserId());
        assertEquals(testAddress.getName(), result.getName());
    }

    /**
     * Test retrieving address by ID and user ID - not found case
     */
    @Test
    void getAddressById_NotFound() {
        // Arrange
        when(addressRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            addressService.getAddressById("user123", 1);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Address not found", exception.getMessage());
    }

    /**
     * Test updating address - successful case
     */
    @Test
    void updateAddress_Success() {
        // Arrange
        Address updatedAddress = new Address();
        updatedAddress.setId(1);
        updatedAddress.setUserId("user123");
        updatedAddress.setName("Work");
        updatedAddress.setAddress("456 Business Ave");
        updatedAddress.setDetails("Floor 10");
        updatedAddress.setLatitude(BigDecimal.valueOf(40.7128));
        updatedAddress.setLongitude(BigDecimal.valueOf(-74.0060));

        when(addressRepository.findById(1)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        // Act
        Address result = addressService.updateAddress(1, updatedAddress);

        // Assert
        assertEquals(updatedAddress.getId(), result.getId());
        assertEquals(updatedAddress.getName(), result.getName());
        assertEquals(updatedAddress.getAddress(), result.getAddress());
        assertEquals(updatedAddress.getDetails(), result.getDetails());
    }

    /**
     * Test updating address - address not found case
     */
    @Test
    void updateAddress_NotFound() {
        // Arrange
        when(addressRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            addressService.updateAddress(999, testAddress);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Address not found", exception.getMessage());
    }

    /**
     * Test updating address - user ID mismatch case
     */
    @Test
    void updateAddress_UserIdMismatch() {
        // Arrange
        Address differentUserAddress = new Address();
        differentUserAddress.setId(1);
        differentUserAddress.setUserId("different123");

        when(addressRepository.findById(1)).thenReturn(Optional.of(testAddress));

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            addressService.updateAddress(1, differentUserAddress);
        });

        assertEquals(403, exception.getCode());
        assertEquals("User id not match", exception.getMessage());
    }

    /**
     * Test deleting address - successful case
     */
    @Test
    void deleteAddress_Success() {
        // Arrange
        when(addressRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.of(testAddress));

        // Act
        assertDoesNotThrow(() -> {
            addressService.deleteAddress("user123", 1);
        });

        // Assert
        verify(addressRepository).deleteById(1);
    }

    /**
     * Test deleting address - not found case
     */
    @Test
    void deleteAddress_NotFound() {
        // Arrange
        when(addressRepository.findByIdAndUserId(1, "user123")).thenReturn(Optional.empty());

        // Act & Assert
        OperationException exception = assertThrows(OperationException.class, () -> {
            addressService.deleteAddress("user123", 1);
        });

        assertEquals(404, exception.getCode());
        assertEquals("Address not found", exception.getMessage());
    }
}