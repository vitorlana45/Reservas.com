package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.entities.UserRole;
import com.lanaVitor.Reservas.com.services.validation.UserInsertValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@UserInsertValid
public class UserDTO {

    private Long id;
    @Setter
    private String name;
    @Setter
    @Email(message = "O campo Email deve conter o formato correto")
    private String email;

    @NotBlank
    private String password;

    @Getter
    private UserRole role;

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
        role = entity.getRole();
    }
}
