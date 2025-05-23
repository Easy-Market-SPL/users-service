package co.edu.javeriana.easymarket.usersservice.mappers;

import co.edu.javeriana.easymarket.usersservice.dtos.PaymentMethodDTO;
import co.edu.javeriana.easymarket.usersservice.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {
    private final ModelMapper modelMapper;

    public PaymentMethodMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PaymentMethodDTO paymentMethodToPaymentMethodDTO(PaymentMethod paymentMethod) {
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    public PaymentMethod paymentMethodDTOToPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        return modelMapper.map(paymentMethodDTO, PaymentMethod.class);
    }
}
