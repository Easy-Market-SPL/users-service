package co.edu.javeriana.easymarket.usersservice.repository;

import co.edu.javeriana.easymarket.usersservice.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethod> findByUserId(String userId);
    Optional<PaymentMethod> findByIdAndUserId(Integer id, String userId);
}