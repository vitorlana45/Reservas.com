package tests;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Factory {


    public static Hotel createHotel() {
        List<User> list = new ArrayList<>();
        list.add(createUser());
        var rooms = createExpectedRooms();
        return new Hotel(1L, "TestHotel", "Sao Paulo", "Otimo hotel", "Disponivel",rooms, list);
    }

    public static HotelInfoDTO createHotelInfoDTO() {
        Hotel hotel = createHotel();
        return new HotelInfoDTO(hotel);
    }

    public static HotelDTO createHotelDTO() {
        HotelDTO dto = new HotelDTO(Factory.createHotel());
        return dto;
    }

    public static List<Rooms> createExpectedRooms() {
        List<Rooms> roomsList = new ArrayList<>();

        Rooms room = new Rooms();
        room.setId(2L);
        room.setRoomsNumber(1);
        room.setRented(false);
        roomsList.add(room);

        return roomsList;
    }
    public static Rooms createRoom() {
        Date checkInDate = new Date(); // Data atual
        Date checkOutDate = new Date(124, 3, 20); // 2024-04-20 (ano, mês - 1, dia)

        Hotel hotel = createHotel(); // Obter o hotel criado

        Rooms room = new Rooms();
        room.setId(1L);
        room.setRoomsNumber(1);
        room.setCheckIn(checkInDate);
        room.setCheckOut(checkOutDate);
        room.setRented(false);
        room.setHotel(hotel); // Associar o hotel ao quarto

        return room;
    }
    public static List<Hotel> createHotelList() {
        List<Hotel> hotels = new ArrayList<>();

        Hotel hotel1 = new Hotel(1L, "Hotel A", "Local A", "Descrição A");
        Hotel hotel2 = new Hotel(2L, "Hotel B", "Local B", "Descrição B");
        Hotel hotel3 = new Hotel(3L, "Hotel C", "Local C", "Descrição C");

        hotels.add(hotel1);
        hotels.add(hotel2);
        hotels.add(hotel3);

        return hotels;
    }


    public static ReserveRoomsRequestDTO reservationDTO() {
        Date checkIn = new Date(); // Data atual
        Date checkOut = new Date(124, 3, 20); // 2024-04-20 (ano, mês - 1, dia)
        ReservationHotelDTO reservationHotelDTO = new ReservationHotelDTO(checkIn, checkOut, 1L);

        VerificationRegisterDTO verificationRegisterDTO = new VerificationRegisterDTO(createUser().getEmail());

        return new ReserveRoomsRequestDTO(reservationHotelDTO, verificationRegisterDTO);
    }

    public static User createUser() {

        return new User(1L, "name", "email@email.com", "password", null, null);
    }

    public static Hotel createRoomsOccupied (){

        List<User> list = new ArrayList<>();
        list.add(createUser());
        var rooms = createOccupiedRooms();
        return new Hotel(1L, "TestHotel", "Sao Paulo", "Otimo hotel", "Disponivel",rooms, list);
    }

    public static boolean checkAllRoomsOccupied(Hotel hotel) {
        List<Rooms> rooms = hotel.getListRooms();
        return rooms.stream().allMatch(Rooms::isRented);
    }
    public static List<Rooms> createOccupiedRooms() {
        List<Rooms> roomsList = new ArrayList<>();

        Rooms room = new Rooms();
        room.setId(4L);
        room.setRoomsNumber(1);
        room.setRented(true);
        room.setUser(Factory.createUser());
        roomsList.add(room);

        return roomsList;
    }

    public static ReserveRoomsRequestDTO emailNotFoundReservationDTO() {
        Date checkIn = new Date(); // Data atual
        Date checkOut = new Date(124, 3, 20); // 2024-04-20 (ano, mês - 1, dia)
        ReservationHotelDTO reservationHotelDTO = new ReservationHotelDTO(checkIn, checkOut, 1L);

        VerificationRegisterDTO verificationRegisterDTO = new VerificationRegisterDTO(createEmailNotFoundOnDataBase().getEmail());

        return new ReserveRoomsRequestDTO(reservationHotelDTO, verificationRegisterDTO);
    }

    public static User createEmailNotFoundOnDataBase() {

        return new User(1L, "notFound", "notFound@email.com", "password", null, null);
    }


}
