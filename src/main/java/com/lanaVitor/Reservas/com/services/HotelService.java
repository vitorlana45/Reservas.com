package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.InvalidReservationDateException;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import com.lanaVitor.Reservas.com.services.util.UtilService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private static final Integer limitQuantityRoom = 10;
    private final HotelRepository hotelRepository;

    private final HotelRepository repository;
    private final UserRepository userRepository;
    private final RoomsRepository roomsRepository;
    private final EmailService emailService;

    int capturedRoomNumber;

    @Autowired
    public HotelService(HotelRepository repository, UserRepository userRepository, RoomsRepository roomsRepository, EmailService emailService, WebApplicationContext webApplicationContext, HotelRepository hotelRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roomsRepository = roomsRepository;
        this.emailService = emailService;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public CreateHotelDTO create(CreateHotelDTO hotel) {
        var entity = repository.save(convertToCreateHotelDTO(hotel));   //ok
        return new CreateHotelDTO(entity);
    }

    @Transactional(readOnly = true)
    public HotelInfoDTO getInfoResort(Long id) {
        Optional<Hotel> hotel = repository.findById(id);
        Hotel entity = hotel.orElseThrow(() -> new ResourceNotFoundException("hotel nao encontrado")); //ok
        return new HotelInfoDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> getHotelDTOS() {
        List<Hotel> hotels = repository.findAll();
        if (hotels.isEmpty()) throw new ResourceNotFoundException("Recurso nao encontrado!");

        List<HotelDTO> hotelDTOs = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotelDTOs.add(new HotelDTO(hotel));
        }
        return hotelDTOs;
    }

    @Transactional(readOnly = true)
    public HotelDTO searchAllRooms(Long id) {

        Optional<Hotel> hotelOptional = repository.findById(id);
        Hotel hotelEntity = hotelOptional.orElseThrow(() -> new ResourceNotFoundException("Hotel não encontrado"));

        List<Rooms> listRooms = hotelEntity.getListRooms();

        for (Rooms room : listRooms) {
            if (room.isAvailable() && room.getUser() == null) { // método isAvailable() para verificar a disponibilidade
                HotelDTO hotelDTO = new HotelDTO(hotelEntity);
                hotelDTO.getRooms().add(new RoomsDTO(room)); // Adiciona o quarto disponível ao DTO do hotel
            }
        }
        return new HotelDTO(hotelEntity);
    }

    @Transactional
    public void addRooms(Long hotelId, CreateRoomsDTO roomsDTO) {

        Hotel hotel = repository.findById(hotelId).orElseThrow(() -> new EntityNotFoundException("hotel nao existe!"));

        Rooms newRoom = convertRoom(roomsDTO);

        List<Rooms> roomsEntity = hotel.getListRooms();

        roomsEntity.add(newRoom);

        newRoom.setHotel(hotel);

        newRoom.getHotel().setStatus("Disponivel");

        repository.save(hotel);
    }

    @Transactional
    public void reserveRoom(ReserveRoomsRequestDTO data, Long hotelId, ReserveRoomsRequestDTO user) {

        validateCheckInAndCheckOutDates(data);

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
            var newRoom = new Rooms();
            room = roomsRepository.save(newRoom);
//          throw new ResourceNotFoundException("Quarto indisponivel");
        }

        UserDetails entity = verificationUserExists(user.getUser());

        room.setUser((User) entity);
        room.setCheckIn(data.getReservationDTO().getCheckIn());
        room.setCheckOut(data.getReservationDTO().getCheckOut());
        room.setRented(true);

        Rooms savedRoom = roomsRepository.save(room);

        sendConfirmationEmail(data, user);
    }

    @Transactional()
    public void deleteRoom(Long hotelId, Long roomId) {

        Optional<Hotel> hotelOptional = repository.findById(hotelId);
        Hotel hotel = hotelOptional.orElseThrow(() -> new ResourceNotFoundException("Hotel não Encontrado"));

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
    }


    private void validateCheckInAndCheckOutDates(ReserveRoomsRequestDTO data) {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime checkIn = data.getReservationDTO().getCheckIn();
        LocalDateTime checkOut = data.getReservationDTO().getCheckOut();

        ZonedDateTime checkInUtc = checkIn.atZone(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime checkOutUtc = checkOut.atZone(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);


        // Validar se o check-in não está no passado
        if (checkInUtc.isBefore(nowUtc)) {
            throw new InvalidReservationDateException("A data de check-in é inválida. Deve ser no futuro ou presente!");
        }

        // Validar se o check-out não está no passado
        if (checkOutUtc.isBefore(nowUtc)) {
            throw new InvalidReservationDateException("A data de check-out é inválida. Deve ser no futuro ou presente!");
        }

        // Validar se o check-out é após o check-in
        if (checkOutUtc.isBefore(checkInUtc)) {
            throw new InvalidReservationDateException("A data de check-out deve ser após a data de check-in!");
        }
    }

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

    private boolean checkAllRoomsOccupied(Hotel hotel) {
        List<Rooms> rooms = hotel.getListRooms();
        return rooms.stream().allMatch(Rooms::isRented);
    }

    private void sendConfirmationEmail(ReserveRoomsRequestDTO reservationData, ReserveRoomsRequestDTO userData) {
        String totalPrice = UtilService.calculateTotalPrice(reservationData.getReservationDTO().getCheckIn(), reservationData.getReservationDTO().getCheckOut());
        emailService.sendEmailText(userData.getUser().getEmail(), "Confirmação de reserva", totalPrice);
    }

    private Hotel convertToCreateHotelDTO(CreateHotelDTO entity) {
        var hotel = new Hotel();
        hotel.setName(entity.getName());
        hotel.setLocation(entity.getLocation());
        hotel.setDescription(entity.getDescription());
        return hotel;
    }

    private Rooms convertRoom(CreateRoomsDTO roomsDTO) {
        var entity = new Rooms();

        entity.setRoomsNumber(roomsDTO.getRoomsNumber());
        entity.setCheckIn(null);
        entity.setCheckOut(null);

        return entity;
    }

    @Transactional(readOnly = true)
    protected UserDetails verificationUserExists(VerificationRegisterDTO data) {
        try {
            return userRepository.findUserByEmail(data.getEmail());
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("É necessario ter cadastro para reservar um quarto");
        }
    }
    // metodos para outros services poderem consumir sem precisar de injetar o hotelRepository
    @Transactional(readOnly = true)
    public Hotel searchHotelById(long id){
        return hotelRepository.searchHotelById(id);
    }
    @Transactional
    public void saveHotel(Hotel hotel){
        hotelRepository.save(hotel);
    }
}
