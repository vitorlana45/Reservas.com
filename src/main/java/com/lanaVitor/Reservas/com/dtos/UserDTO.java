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
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@UserInsertValid
public class UserDTO {

    private Long id;
    @Setter
    @NotBlank(message = "O campo nome é obrigatório")
    @Length(min = 3, max = 70, message = "O campo nome deve conter entre 3 e 100 caracteres")
    private String name;
    @Setter
    @Email(message = "O campo Email deve conter o formato correto")
    private String email;

    @NotBlank(message = "O campo senha é obrigatório")
    @Length(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
    private String password;

    @Getter
    @Setter
    private UserRole role;

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
        role = entity.getRole();
    }
}
