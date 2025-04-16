package co.edu.javeriana.easymarket.usersservice.controllers;

import co.edu.javeriana.easymarket.usersservice.dtos.AddressDTO;
import co.edu.javeriana.easymarket.usersservice.mappers.AddressMapper;
import co.edu.javeriana.easymarket.usersservice.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    // Get addresses from user id
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAddresses(@PathVariable String userId) {
        List<AddressDTO> addresses = addressService.getAddresses(userId).stream()
                .map(addressMapper::addressToAddressDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }
}
