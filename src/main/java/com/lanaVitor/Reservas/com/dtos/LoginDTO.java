package com.lanaVitor.Reservas.com.dtos;


import com.lanaVitor.Reservas.com.entities.Login;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Random;


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
    private LocalDateTime loginMoment;

    public LoginDTO(Login entity) {
        id = entity.getId();
        email = entity.getEmail();
        password = entity.getPassword();
        this.loginMoment = LocalDateTime.now();
    }
}
