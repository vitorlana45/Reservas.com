package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/resort")
public class HotelController {

    private HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping(("/rooms"))
    @Operation(summary = "Lista de quartos ", description = "Listagem de quartos do Resort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    public ResponseEntity<List<HotelDTO>> getListRooms() {
        List<HotelDTO> list = hotelService.getHotelDTOS();
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Informações do resort", description = "Informações completa do resort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @GetMapping(value = "/{id}")
    public ResponseEntity<HotelInfoDTO> infoResorts(@PathVariable Long id) {
        HotelInfoDTO dto = hotelService.getInfoResort(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/insert")
    @Operation(summary = "Inserção de novos Resorts / hoteis", description = "Inserção de novos resorts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Bad Request")})
    public ResponseEntity<HotelDTO> insertResort(@Valid @RequestBody HotelDTO data) {
        HotelDTO entity = hotelService.insert(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @GetMapping("/{id}/available/rooms")
    @Operation(summary = "Inserção de novos Resorts / hoteis", description = "Inserção de novos resorts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisnição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    public ResponseEntity<List<HotelDTO>> availableRooms(@PathVariable Long id) {
        List<HotelDTO> list = hotelService.searchAvailableRooms(id);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ResponseRentedRoom> reserveRooms(@PathVariable Long id, @RequestBody ReserveRoomsRequestDTO requestDTO) {

        ResponseRentedRoom data = hotelService.reserveRoom(requestDTO, id, requestDTO);
        return ResponseEntity.ok().body(data);
    }


    @DeleteMapping("/{resortId}/Room/{roomId}")
    public ResponseEntity<Void> restoreRoom(@PathVariable Long resortId, @PathVariable Long roomId) {
        hotelService.deleteRoom(resortId, roomId);

        return ResponseEntity.ok().build();
    }

}
