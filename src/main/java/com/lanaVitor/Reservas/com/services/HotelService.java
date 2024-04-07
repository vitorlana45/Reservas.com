package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class HotelService {

    private HotelRepository repository;
    private UserRepository userRepositoy;

    private RoomsRepository roomsRepository;

    @Autowired
    public HotelService(HotelRepository repository, UserRepository userRepository, RoomsRepository roomsRepository) {
        this.repository = repository;
        this.userRepositoy = userRepository;
        this.roomsRepository = roomsRepository;
    }

    public HotelInfoDTO getInfoResort(Long id) {
        Optional<Hotel> hotel = repository.findById(id);
        Hotel entity = hotel.orElseThrow(() -> new RuntimeException("hotel nao encontrado"));
        return new HotelInfoDTO(entity);
    }

    public List<HotelDTO> getHotelDTOS() {
        List<Hotel> hotels = repository.findAll();
        List<HotelDTO> hotelDTOs = new ArrayList<>();

        for (Hotel hotel : hotels) {
            List<Rooms> roomsList = hotel.getListRooms();
            hotelDTOs.add(new HotelDTO(hotel, roomsList));
        }
        return hotelDTOs;
    }

    public HotelDTO insert(HotelDTO entity) {
        Hotel data = repository.save(convertHotelDtoToHotel(entity));
        return new HotelDTO(data);
    }

    public List<HotelDTO> searchAvailableRooms(Long id) {

        Optional<Hotel> hotelOptional = repository.findById(id);
        Hotel hotelEntity = hotelOptional.orElseThrow(() -> new RuntimeException("Hotel não encontrado"));

        List<Rooms> listRooms = hotelEntity.getListRooms();
        List<HotelDTO> availableRooms = new ArrayList<>();

        for (Rooms room : listRooms) {
            if (!room.isAvailable() & room.getUser() == null) { // método isAvailable() para verificar a disponibilidade
                HotelDTO hotelDTO = new HotelDTO(hotelEntity);
                hotelDTO.getRooms().add(new RoomsDTO(room)); // Adiciona o quarto disponível ao DTO do hotel
                availableRooms.add(hotelDTO);
            }
        }
        return availableRooms;
    }

    public List<ResponseRentedRoom> reserveRooms(ReserveRoomsRequestDTO data, Long id, ReserveRoomsRequestDTO user) {
        List<Long> recusedNumberRooms = new ArrayList<>();
        Optional<Hotel> hotel = repository.findById(id);
        Hotel hotelEntity = hotel.orElseThrow(() -> new RuntimeException("Hotel não encontrado"));

        List<Rooms> rooms = hotelEntity.getListRooms();
        List<Rooms> availableRooms = new ArrayList<>();
        List<Rooms> reservedForUser = new ArrayList<>();

        for (Rooms room : rooms) {
            if (!room.isAvailable() && room.getUser() == null) {
                availableRooms.add(room);
            }else {
                recusedNumberRooms.add(room.getId());
            }
        }

        for (Rooms room : availableRooms) {
            if (data.getReservationDTO() != null && data.getReservationDTO().getListReservation().contains(room.getId())) {
                reservedForUser.add(room);
                room.setCheckIn(data.getReservationDTO().getCheckIn());
                room.setCheckOut(data.getReservationDTO().getCheckOut());
                room.setRented(true);
            }
        }
        User entity = verificationUserExists(user.getUser());
        for (Rooms reserve : reservedForUser) {
            reserve.setUser(entity);
        }
        roomsRepository.saveAll(reservedForUser);

        // Verifica se há quartos reservados
        if (!reservedForUser.isEmpty()) {
            List<ResponseRentedRoom> list = new ArrayList<>();
            list.add(new ResponseRentedRoom(reservedForUser));
            return list;
        } else {
            throw new ResourceNotFoundException("recurso nao encontrado!");
        }
    }
    private Hotel convertHotelDtoToHotel(HotelDTO data) {
        Hotel entity = new Hotel();
        entity.setName(data.getName());
        entity.setLocation(data.getLocation());
        entity.setDescription(data.getDescription());
        return entity;
    }


    private User verificationUserExists(VerificationRegisterDTO data) {
        try {
            User user = new User();
            user.setEmail(data.getEmail());
            return userRepositoy.findByEmail(user.getEmail());
        } catch (RuntimeException e) {
            throw new RuntimeException("É nescessario ter cadastro para reservar um quarto");
        }
    }
}
