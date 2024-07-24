package com.lanaVitor.Reservas.com.dtos;


import com.lanaVitor.Reservas.com.entities.Rooms;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomsDTO {

    @NotBlank
    @NotNull
    private Integer roomsNumber;

    public CreateRoomsDTO (Rooms rooms){
        roomsNumber = rooms.getRoomsNumber();
    }
}
