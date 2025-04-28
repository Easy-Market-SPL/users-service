package co.edu.javeriana.easymarket.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {
    private Integer id;
    private String cardNumber;
    private String email;
    private String phone;
    private String expiryDate;
    private String cardHolderName;
    private String city;
    private String firstLine;
    private String secondLine;
    private String country;
    private String postalCode;
    private String stateName;
}
