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
public class HotelDTO {

    private Long id;
    @Setter
    private String name;
    @Setter
    private String location;
    @Setter
    private String description;

    private List<RoomsDTO> rooms = new ArrayList<>();

//  private List<UserDTO> userListDTO = new ArrayList<>();


    public HotelDTO(Hotel hotel) {
        id = hotel.getId();
        name = hotel.getName();
        location = hotel.getLocation();
        description = hotel.getDescription();
    }

    public HotelDTO(Hotel hotel, List<Rooms> rooms) {
        this(hotel);
        rooms.forEach(r -> this.rooms.add(new RoomsDTO(r)));
    }

//    public HotelDTO(Hotel hotel, List<Rooms> rooms, UserDTO user) {
//        this(hotel);
//        rooms.forEach(r -> this.rooms.add(new RoomsDTO(r)));
//        userListDTO.add(user);
//    }


}
