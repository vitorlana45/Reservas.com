package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.HotelInfoDTO;
import com.lanaVitor.Reservas.com.dtos.RoomsDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.services.HotelService;
import com.lanaVitor.Reservas.com.services.RoomsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resort")
public class HotelController {
    private final RoomsService roomsService;
    private HotelService hotelService;

    @Autowired
    public HotelController(RoomsService roomsService, HotelService hotelService) {
        this.roomsService = roomsService;
        this.hotelService = hotelService;
    }

    @GetMapping(("/rooms"))
    public ResponseEntity<List<HotelDTO>> getListRooms() {
        List<HotelDTO> list = hotelService.getHotelDTOS();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HotelInfoDTO> infoResorts(@PathVariable Long id) {
        HotelInfoDTO dto = hotelService.getInfoResort(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/insert")
    public ResponseEntity<HotelDTO> insertResort(@RequestBody HotelDTO data) {
        HotelDTO entity = hotelService.insert(data);
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("/{id}/available/rooms")
    public ResponseEntity<List<HotelDTO>> availableRooms(@PathVariable Long id) {
        List<HotelDTO> list = hotelService.searchAvailableRooms(id);
        return ResponseEntity.ok().body(list);
    }

}
