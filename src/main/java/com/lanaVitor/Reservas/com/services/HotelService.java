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

    private static final Integer limitQuantityRoom = 10;

    private HotelRepository repository;
    private UserRepository userRepositoy;

    private RoomsRepository roomsRepository;

    private EmailService emailService;

    int capturedRoomNumber;

    @Autowired
    public HotelService(HotelRepository repository, UserRepository userRepository, RoomsRepository roomsRepository, EmailService emailService) {
        this.repository = repository;
        this.userRepositoy = userRepository;
        this.roomsRepository = roomsRepository;
        this.emailService = emailService;
    }

    public HotelInfoDTO getInfoResort(Long id) {
        Optional<Hotel> hotel = repository.findById(id);
        Hotel entity = hotel.orElseThrow(() -> new ResourceNotFoundException("hotel nao encontrado"));
        return new HotelInfoDTO(entity);
    }

    public List<HotelDTO> getHotelDTOS() {
        List<Hotel> hotels = repository.findAll();
        if (hotels.isEmpty()) throw new ResourceNotFoundException("Recurso nao encontrado!");

        List<HotelDTO> hotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelDTOs.add(new HotelDTO(hotel));
        }
        return hotelDTOs;
    }

    public HotelDTO insert(HotelDTO entity) {

        if (entity == null) {
            throw new NullPointerException("Dados nullos");
        } else {
            Hotel data = repository.save(convertHotelDtoToHotel(entity));
            return new HotelDTO(data);
        }
    }

    public List<HotelDTO> searchAvailableRooms(Long id) {

        Optional<Hotel> hotelOptional = repository.findById(id);
        Hotel hotelEntity = hotelOptional.orElseThrow(() -> new ResourceNotFoundException("Hotel não encontrado"));

        List<Rooms> listRooms = hotelEntity.getListRooms();
        List<HotelDTO> availableRooms = new ArrayList<>();

        for (Rooms room : listRooms) {
            if (room.isAvailable() & room.getUser() == null) { // método isAvailable() para verificar a disponibilidade
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

        Rooms room = findAvailableRoomAndUpdateStatus(hotelEntity, data.getReservationDTO().getNumberRoom());

        boolean allRoomsOccupied = checkAllRoomsOccupied(hotelEntity);
        if (allRoomsOccupied) {
            hotelEntity.setStatus("Cheio");
            repository.save(hotelEntity);
        } else {
            hotelEntity.setStatus("Disponível");
            repository.save(hotelEntity);
        }

        if (room == null) {
            throw new ResourceNotFoundException("Quarto indisponivel");
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

    // Método para verificar se todos os quartos do hotel estão ocupados
    private boolean checkAllRoomsOccupied(Hotel hotel) {
        List<Rooms> rooms = hotel.getListRooms();
        return rooms.stream().allMatch(Rooms::isRented);
    }

    // Método para encontrar um quarto disponível e atualizar o status do hotel
    private Rooms findAvailableRoomAndUpdateStatus(Hotel hotel, Long roomId) {
        List<Rooms> rooms = hotel.getListRooms();
        Rooms room = rooms.stream()
                .filter(r -> r.getId().equals(roomId) && r.getUser() == null)
                .findFirst()
                .orElse(null);

        if (room != null) {
            room.setRented(true);
            return room;
        }
        return null;
    }

    @Transactional
    public void deleteRoom(Long hotelId, Long roomId) {

        Optional<Hotel> hotelOptional = repository.findById(hotelId);
        Hotel hotel = hotelOptional.orElseThrow(() -> new ResourceNotFoundException("Hotel não encontrado"));

        List<Rooms> hotelListRooms = hotel.getListRooms();

        boolean isRoomRemoved = hotelListRooms.removeIf(room -> {

            if (room.getId().equals(roomId)) {
                this.capturedRoomNumber = room.getRoomsNumber();
                return true;
            }
            return false;
        });
        if (!isRoomRemoved) {
            throw new ResourceNotFoundException("Quarto não encontrado no hotel.");
        }

        try {
            roomsRepository.deleteById(roomId);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Erro ao excluir o quarto.");
        }

        List<Rooms> listVerification = roomsRepository.findAll();
        int roomsToAdd = limitQuantityRoom - listVerification.size();
        for (int i = 0; i < roomsToAdd; i++) {
            roomsRepository.save(new Rooms(null, capturedRoomNumber, null,null,false,null,null));
        }
    }

    private void sendConfirmationEmail(ReserveRoomsRequestDTO reservationData, ReserveRoomsRequestDTO userData) {
        String totalPrice = UtilService.calculateTotalPrice(reservationData.getReservationDTO().getCheckIn(), reservationData.getReservationDTO().getCheckOut());
        User user = verificationUserExists(userData.getUser());
        if (user != null) {
            emailService.sendEmailText(userData.getUser().getEmail(), "Confirmação de reserva", totalPrice);
        } else {
            throw new ResourceNotFoundException("para reservar um quarto e nescessario ter cadastro!");
        }
    }

    private Hotel convertHotelDtoToHotel(HotelDTO data) {
        Hotel entity = new Hotel();
        entity.setName(data.getName());
        entity.setLocation(data.getLocation());
        entity.setDescription(data.getDescription());

        // Inicializa a lista de quartos na entidade Hotel
        List<Rooms> roomsList = new ArrayList<>();

        int count = 0;
        // Converte cada RoomsDTO para Rooms e adiciona à lista
        for (RoomsDTO roomsDTO : data.getRooms()) {
            Rooms rooms = new Rooms();
            rooms.setId(roomsDTO.getId());
            rooms.setRoomsNumber(roomsDTO.getRoomsNumber());
            String rented = roomsDTO.getRented();
            if (rented.equals("false")) {
                count++;
            }
            roomsList.add(rooms);
        }

        // Define a lista de quartos na entidade Hotel
        entity.setListRooms(roomsList);
        entity.setStatus(count != 0 ? "Disponivel" : "Cheio");
        return entity;
    }

    private User verificationUserExists(VerificationRegisterDTO data) {
        try {
            User user = new User();
            user.setEmail(data.getEmail());
            return userRepositoy.findByEmail(user.getEmail());
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("É necessario ter cadastro para reservar um quarto");
        }
    }
}
