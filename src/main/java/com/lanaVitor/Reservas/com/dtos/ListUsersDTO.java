package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class ListUsersDTO {

    private Long id;
    private String name;
    private String email;
    private UserRole role;

}
