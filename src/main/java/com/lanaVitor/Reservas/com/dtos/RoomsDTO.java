package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoomsDTO {

    @Setter
    private Long id;
    @Setter
    private Integer roomsNumber;

    private String rented;

    public RoomsDTO(Rooms rooms) {
        this.id = rooms.getId();
        this.roomsNumber = rooms.getRoomsNumber();
        this.rented = (!rooms.isAvailable()) ?"Disponivel" : "Reservado";
    }

    public RoomsDTO(Rooms rooms, User user) {
        this.id = rooms.getId();
        this.roomsNumber = rooms.getRoomsNumber();
    }
}

