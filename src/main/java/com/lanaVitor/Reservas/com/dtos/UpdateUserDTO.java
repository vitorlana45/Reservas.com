package com.lanaVitor.Reservas.com.dtos;


import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.entities.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UpdateUserDTO {

    private Long id;
    @Setter

    private String name;
    @Setter

    @Email(message = "O campo Email deve conter o formato correto")
    private String email;

    @NotBlank
    @NotNull
    private String password;

    private UserRole role;

    public UpdateUserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
        role = entity.getRole();
    }
}

