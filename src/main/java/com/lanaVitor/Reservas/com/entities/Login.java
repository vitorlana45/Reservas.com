package com.lanaVitor.Reservas.com.entities;


import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String email;
    private String password;
    private LocalDateTime loginMoment;

    public Login(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.loginMoment = LocalDateTime.now();
    }

    public Login(LoginDTO entity) {
        id = entity.getId();
        email = entity.getEmail();
        loginMoment = LocalDateTime.now();
    }
}
