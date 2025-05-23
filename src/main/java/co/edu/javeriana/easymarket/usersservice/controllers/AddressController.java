package co.edu.javeriana.easymarket.usersservice.controllers;

import co.edu.javeriana.easymarket.usersservice.dtos.AddressDTO;
import co.edu.javeriana.easymarket.usersservice.mappers.AddressMapper;
import co.edu.javeriana.easymarket.usersservice.model.Address;
import co.edu.javeriana.easymarket.usersservice.services.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    // Get addresses from user id
    @GetMapping
    public ResponseEntity<?> getAddresses(@PathVariable String userId) {
        List<AddressDTO> addresses = addressService.getAddresses(userId).stream()
                .map(addressMapper::addressToAddressDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }

    // Get address by id
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(@PathVariable String userId, @PathVariable Integer addressId) {
        AddressDTO address = addressMapper.addressToAddressDTO(
                addressService.getAddressById(userId, addressId)
        );
        return ResponseEntity.ok(address);
    }

    // Create a new address for a user
    @PostMapping
    public ResponseEntity<?> createAddress(@PathVariable String userId, @RequestBody AddressDTO addressDTO) {
        Address address = addressMapper.addressDTOToAddress(addressDTO);
        address.setUserId(userId);
        Address createdAddress = addressService.createAddress(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressMapper.addressToAddressDTO(createdAddress));
    }

    // Update an existing address
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable String userId,
            @PathVariable Integer addressId,
            @RequestBody AddressDTO addressDTO) {
        Address address = addressMapper.addressDTOToAddress(addressDTO);
        address.setUserId(userId);
        Address updated = addressService.updateAddress(addressId, address);
        return ResponseEntity.ok(addressMapper.addressToAddressDTO(updated));
    }

    // Delete an address
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable String userId, @PathVariable Integer addressId) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
