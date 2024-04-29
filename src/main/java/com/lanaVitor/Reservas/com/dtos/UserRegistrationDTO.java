package com.lanaVitor.Reservas.com.dtos;


import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.entities.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {
    private Long id;
    private String name;
    private String email;
    private UserRole role;

    public UserRegistrationDTO(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
       role = user.getRole();
    }

}
