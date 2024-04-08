package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import com.lanaVitor.Reservas.com.services.util.UtilService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    private EmailService emailService;

    @Autowired
    public HotelService(HotelRepository repository, UserRepository userRepository, RoomsRepository roomsRepository, EmailService emailService) {
        this.repository = repository;
        this.userRepositoy = userRepository;
        this.roomsRepository = roomsRepository;
        this.emailService = emailService;
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

    @Transactional
    public ResponseRentedRoom reserveRoom(ReserveRoomsRequestDTO data, Long hotelId, ReserveRoomsRequestDTO user) {
        Optional<Hotel> hotelOptional = repository.findById(hotelId);
        Hotel hotelEntity = hotelOptional.orElseThrow(() -> new ResourceNotFoundException("Hotel não encontrado"));

        Rooms room = findAvailableRoom(hotelEntity, data.getReservationDTO().getNumberRoom());
        if (room == null) {
            throw new ResourceNotFoundException("Quarto não disponível para reserva.");
        }

        User entity = verificationUserExists(user.getUser());
        room.setUser(entity);
        room.setCheckIn(data.getReservationDTO().getCheckIn());
        room.setCheckOut(data.getReservationDTO().getCheckOut());
        room.setRented(true);

        Rooms savedRoom = roomsRepository.save(room);
        if (savedRoom != null) {
            sendConfirmationEmail(data, user);
            return new ResponseRentedRoom(savedRoom);
        } else {
            throw new RuntimeException("Erro ao salvar quarto reservado.");
        }
    }

    private Rooms findAvailableRoom(Hotel hotel, Long roomId) {
        return hotel.getListRooms().stream()
                .filter(room -> room.getId().equals(roomId) && !room.isRented() && room.getUser() == null)
                .findFirst()
                .orElse(null);
    }

    public void sendConfirmationEmail(ReserveRoomsRequestDTO reservationData, ReserveRoomsRequestDTO userData) {
        String totalPrice = UtilService.calculateTotalPrice(reservationData.getReservationDTO().getCheckIn(), reservationData.getReservationDTO().getCheckOut());
        User user =  verificationUserExists(userData.getUser());
        if(user != null){
            emailService.sendEmailText(userData.getUser().getEmail(), "Confirmação de reserva", totalPrice);
        }else {
            throw new ResourceNotFoundException("para reservar um quarto e nescessario ter cadastro!");
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
            throw new EntityNotFoundException("É nescessario ter cadastro para reservar um quarto");
        }
    }
}
