package co.edu.javeriana.easymarket.usersservice.repository;

import co.edu.javeriana.easymarket.usersservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUserId(String userId);
}