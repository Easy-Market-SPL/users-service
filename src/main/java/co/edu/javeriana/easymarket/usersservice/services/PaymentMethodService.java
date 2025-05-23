package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.PaymentMethod;
import co.edu.javeriana.easymarket.usersservice.repository.PaymentMethodRepository;
import co.edu.javeriana.easymarket.usersservice.repository.UserRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, UserRepository userRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    /// METHODS FOR PAYMENT METHOD SERVICE
    // Get payment methods by user id
    public List<PaymentMethod> getPaymentMethodsByUserId(String userId) {
        return paymentMethodRepository.findByUserId(userId);
    }

    // Get payment method by id
    public PaymentMethod getPaymentMethodById(Integer paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new OperationException(404, "Payment method not found"));
    }

    // Create Payment Method
    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        // Check if user exists
        if (!userRepository.existsById(paymentMethod.getUserId())) {
            throw new OperationException(404, "User not found");
        }

        // Save the payment method
        try {
            return paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw new OperationException(400, "Error creating payment method " + e.getMessage());
        }
    }

    // Delete Payment Method by id
    public void deletePaymentMethod(String userId, Integer paymentMethodId) {
        paymentMethodRepository.findByIdAndUserId(paymentMethodId, userId).orElseThrow(() -> new OperationException(404, "Payment method or user not found"));
        paymentMethodRepository.deleteById(paymentMethodId);
    }

    // Update Payment Method
    public PaymentMethod updatePaymentMethod(Integer paymentMethodId, PaymentMethod paymentMethod) {
        // Check if the payment method exists
        PaymentMethod existingPaymentMethod = getPaymentMethodById(paymentMethodId);

        // Check if user exists
        if (!userRepository.existsById(paymentMethod.getUserId())) {
            throw new OperationException(404, "User not found");
        }

        // Update the payment method fields if they are not null
        if (paymentMethod.getCardNumber() != null) {
            existingPaymentMethod.setCardNumber(paymentMethod.getCardNumber());
        }

        if (paymentMethod.getExpiryDate() != null) {
            existingPaymentMethod.setExpiryDate(paymentMethod.getExpiryDate());
        }

        if (paymentMethod.getCardHolderName() != null) {
            existingPaymentMethod.setCardHolderName(paymentMethod.getCardHolderName());
        }

        if (paymentMethod.getEmail() != null) {
            existingPaymentMethod.setEmail(paymentMethod.getEmail());
        }

        if (paymentMethod.getPhone() != null) {
            existingPaymentMethod.setPhone(paymentMethod.getPhone());
        }

        if (paymentMethod.getCity() != null) {
            existingPaymentMethod.setCity(paymentMethod.getCity());
        }

        if (paymentMethod.getFirstLine() != null) {
            existingPaymentMethod.setFirstLine(paymentMethod.getFirstLine());
        }

        if (paymentMethod.getSecondLine() != null) {
            existingPaymentMethod.setSecondLine(paymentMethod.getSecondLine());
        }

        if (paymentMethod.getCountry() != null) {
            existingPaymentMethod.setCountry(paymentMethod.getCountry());
        }

        if (paymentMethod.getPostalCode() != null) {
            existingPaymentMethod.setPostalCode(paymentMethod.getPostalCode());
        }

        if (paymentMethod.getStateName() != null) {
            existingPaymentMethod.setStateName(paymentMethod.getStateName());
        }

        // Save the updated payment method
        return paymentMethodRepository.save(existingPaymentMethod);
    }
}
