package co.edu.javeriana.easymarket.usersservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_id_gen")
    @SequenceGenerator(name = "payment_method_id_gen", sequenceName = "payment_method_id_payment_method_seq", allocationSize = 1)
    @Column(name = "id_payment_method", nullable = false)
    private Integer id;

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "expiry_date", nullable = false, length = 6)
    private String expiryDate;

    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderName;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "first_line", nullable = false, length = 100)
    private String firstLine;

    @Column(name = "second_line", nullable = false, length = 100)
    private String secondLine;

    @Column(name = "country", nullable = false, length = 25)
    private String country;

    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "state_name", nullable = false, length = 25)
    private String stateName;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
}