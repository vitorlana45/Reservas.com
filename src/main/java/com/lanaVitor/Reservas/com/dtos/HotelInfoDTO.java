package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDTO {

    private Long id;
    @Setter
    private String name;
    @Setter
    private String location;
    @Setter
    private String description;

    private String status;

    public HotelInfoDTO(Hotel hotel) {
        id = hotel.getId();
        name = hotel.getName();
        location = hotel.getLocation();
        description = hotel.getDescription();
        status = verificationStatus(hotel);
    }

    public String verificationStatus(Hotel hotel) {
        List<Integer> count = new ArrayList<>();
        List<Rooms> roomsList = hotel.getListRooms();
        int sum = 1;
        for (Rooms list : roomsList) {
            if (list.isAvailable()) {
                count.add(sum++);
            }
        }
        return (count.isEmpty()) ? "Cheio" : "Disponivel";
    }
}
