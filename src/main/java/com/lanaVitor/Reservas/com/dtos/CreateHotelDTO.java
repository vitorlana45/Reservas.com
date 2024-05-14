package com.lanaVitor.Reservas.com.dtos;

import com.lanaVitor.Reservas.com.entities.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateHotelDTO {

    private String name;
    private String location;
    private String description;

    public CreateHotelDTO(Hotel entity ){
        name = entity.getName();
        location = entity.getLocation();
        description = entity.getDescription();
    }

}
