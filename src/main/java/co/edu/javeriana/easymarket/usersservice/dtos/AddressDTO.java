package co.edu.javeriana.easymarket.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    private Integer id;
    private String name;
    private String address;
    private String details;
    private BigDecimal latitude;
    private BigDecimal longitude;
}