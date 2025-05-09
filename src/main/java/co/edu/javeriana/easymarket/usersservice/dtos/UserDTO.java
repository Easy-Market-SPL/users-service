package co.edu.javeriana.easymarket.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String fullname;
    private String email;
    private String rol;
}