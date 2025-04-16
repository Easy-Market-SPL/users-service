package co.edu.javeriana.easymarket.usersservice.mappers;

import co.edu.javeriana.easymarket.usersservice.dtos.AddressDTO;
import co.edu.javeriana.easymarket.usersservice.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AddressDTO addressToAddressDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }

    public Address addressDTOToAddress(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, Address.class);
    }
}
