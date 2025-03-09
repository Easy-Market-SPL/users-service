package co.edu.javeriana.easymarket.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserDTO {
    private String id;
    private String username;
    private String fullname;
    private String email;
}
