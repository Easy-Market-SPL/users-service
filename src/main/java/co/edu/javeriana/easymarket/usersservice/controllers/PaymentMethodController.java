package co.edu.javeriana.easymarket.usersservice.controllers;

import co.edu.javeriana.easymarket.usersservice.dtos.PaymentMethodDTO;
import co.edu.javeriana.easymarket.usersservice.mappers.PaymentMethodMapper;
import co.edu.javeriana.easymarket.usersservice.model.PaymentMethod;
import co.edu.javeriana.easymarket.usersservice.services.PaymentMethodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/{userId}/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodController(PaymentMethodService paymentMethodService, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodService = paymentMethodService;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    // Get payment methods from user id
    @GetMapping
    public ResponseEntity<?> getPaymentMethods(@PathVariable String userId) {
        List<PaymentMethodDTO> paymentMethods = paymentMethodService.getPaymentMethodsByUserId(userId).stream()
                .map(paymentMethodMapper::paymentMethodToPaymentMethodDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentMethods);
    }

    // Get payment method by id
    @GetMapping("/{paymentMethodId}")
    public ResponseEntity<?> getPaymentMethod(@PathVariable Integer paymentMethodId) {
        PaymentMethodDTO paymentMethod = paymentMethodMapper.paymentMethodToPaymentMethodDTO(
                paymentMethodService.getPaymentMethodById(paymentMethodId)
        );
        return ResponseEntity.ok(paymentMethod);
    }

    // Create a new payment method for a user
    @PostMapping
    public ResponseEntity<?> createPaymentMethod(@PathVariable String userId, @RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodMapper.paymentMethodDTOToPaymentMethod(paymentMethodDTO);
        paymentMethod.setUserId(userId);
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodMapper.paymentMethodToPaymentMethodDTO(createdPaymentMethod));
    }


    // Update an existing payment method
    @PutMapping("/{paymentMethodId}")
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable String userId,
            @PathVariable Integer paymentMethodId,
            @RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodMapper.paymentMethodDTOToPaymentMethod(paymentMethodDTO);
        paymentMethod.setUserId(userId);
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethodId, paymentMethod);
        return ResponseEntity.ok(paymentMethodMapper.paymentMethodToPaymentMethodDTO(updatedPaymentMethod));
    }

    // Delete a payment method
    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable String userId, @PathVariable Integer paymentMethodId) {
        paymentMethodService.deletePaymentMethod(userId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
}
