package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RoomsDTO {

    private Long id;
    private Integer roomsNumber;
    private String rented;

    public RoomsDTO(Rooms rooms) {
        this.id = rooms.getId();
        this.roomsNumber = rooms.getRoomsNumber();
        this.rented = (rooms.isAvailable()) ?"Disponivel" : "Reservado";
    }

    public RoomsDTO(Rooms rooms, User user) {
        this.id = rooms.getId();
        this.roomsNumber = rooms.getRoomsNumber();
    }
}

