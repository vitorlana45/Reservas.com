package tests;

import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;

import java.util.ArrayList;


public class Factory {


    public static Hotel createHotel() {
        return new Hotel(1L, "TestHotel", "Sao paulo", "Otimo hotel", "Disponivel", new ArrayList<>(), null);
    }


    public static HotelInfoDTO createHotelInfoDTO() {
        Hotel hotel = createHotel();
        return new HotelInfoDTO(hotel);
    }

    public static Rooms ListRooms() {
        return new Rooms(1L, 1, null, null, false, null, null);
    }

}
