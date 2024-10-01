package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Login;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Validated
public class LoginDTO {

    private Long id;
    @Email(message = "deve ser um endereço de e-mail valido, verifique os dados novamente!")
    private String email;
    private String password;

    public LoginDTO(Login entity) {
        id = entity.getId();
        email = entity.getUser().getEmail();
        password = entity.getUser().getPassword();
    }
}
