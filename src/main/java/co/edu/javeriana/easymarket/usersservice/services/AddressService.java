package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.Address;
import co.edu.javeriana.easymarket.usersservice.repository.AddressRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // Get all addresses from user id
    public List<Address> getAddresses(String userId) {
        return addressRepository.findByUserId(userId);
    }

    // Create address
    public Address createAddress(Address address) {
        try{
            return addressRepository.save(address);
        }
        catch (Exception e) {
            throw new OperationException(400, "User id not exists");
        }
    }

    public Address getAddressById(String userId, Integer addressId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new OperationException(404, "Address not found"));
    }


    public Address updateAddress(Integer addressId, Address address) {
        Address updatedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new OperationException(404, "Address not found"));

        // If trying to update userId, throw exception
        if(!address.getUserId().equals(updatedAddress.getUserId())){
            throw new OperationException(403, "User id not match");
        }

        // Update the address fields if they are not null
        if(address.getName() != null)
            updatedAddress.setName(address.getName());

        if(address.getAddress() != null)
            updatedAddress.setAddress(address.getAddress());

        if(address.getDetails() != null)
            updatedAddress.setDetails(address.getDetails());

        if(address.getLatitude() != null)
            updatedAddress.setLatitude(address.getLatitude());

        if(address.getLongitude() != null)
            updatedAddress.setLongitude(address.getLongitude());

        return addressRepository.save(updatedAddress);
    }

    public void deleteAddress(String userId, Integer addressId) {
        addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new OperationException(404, "Address not found"));
        addressRepository.deleteById(addressId);
    }
}
