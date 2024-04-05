package com.lanaVitor.Reservas.com.dtos;


import com.lanaVitor.Reservas.com.entities.Login;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Random;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginDTO {

    private Long id;
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
