package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateResponse {

    private String name;
    private String email;

    public UserUpdateResponse(User user) {
        name = user.getName();
        email = user.getEmail();
    }
}
