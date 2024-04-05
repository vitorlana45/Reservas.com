package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.dtos.RoomsDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class HotelService {

    private HotelRepository repository;

    @Autowired
    public HotelService(HotelRepository repository) {
        this.repository = repository;
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

    public Hotel convertHotelDtoToHotel(HotelDTO data) {
        Hotel entity = new Hotel();
        entity.setName(data.getName());
        entity.setLocation(data.getLocation());
        entity.setDescription(data.getDescription());
        return entity;
    }

}
