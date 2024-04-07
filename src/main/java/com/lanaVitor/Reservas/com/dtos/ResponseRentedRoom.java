package com.lanaVitor.Reservas.com.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseRentedRoom {


    private List<Rooms> rooms;

    public ResponseRentedRoom(List<Rooms> reservedForUser) {
        this.rooms = new ArrayList<>();
        this.rooms.addAll(reservedForUser);
    }
}


