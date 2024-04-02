package com.lanaVitor.Reservas.com.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Data
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

    public Login(User entity) {
        id = entity.getId();
        email = entity.getEmail();
        password = entity.getPassword();
        loginMoment = LocalDateTime.now();
    }
}
