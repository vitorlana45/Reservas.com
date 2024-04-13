package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class HotelDTO {

    private Long id;
    @Setter
    private String name;
    @Setter
    private String location;
    @Setter
    private String description;

    private List<RoomsDTO> rooms = new ArrayList<>();


    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.location = hotel.getLocation();
        this.description = hotel.getDescription();
        hotel.getListRooms().forEach(room -> this.rooms.add(new RoomsDTO(room)));
    }

    public HotelDTO(Hotel hotel, List<Rooms> entity) {
        this(hotel);
        entity.forEach(r -> this.rooms.add(new RoomsDTO(r)));
    }
}
