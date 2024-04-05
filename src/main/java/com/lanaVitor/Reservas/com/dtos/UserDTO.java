package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;

    private List<User> usersListDTO = new ArrayList<>();

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        password = entity.getPassword();
    }

    public void AddUserListToDTO(User entity) {
        usersListDTO.add(entity);
    }

}
