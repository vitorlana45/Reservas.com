package com.lanaVitor.Reservas.com.entities;


import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime loginMoment = LocalDateTime.now();

    public Login(LoginDTO entity) {
        id = entity.getId();
        loginMoment = LocalDateTime.now();
    }
}
