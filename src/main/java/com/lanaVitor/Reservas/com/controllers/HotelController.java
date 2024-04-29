package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.Response;
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
    @Operation(summary = "Lista de quartos ", description = "Listagem de quartos do Resort, retorna o status de cada quarto")
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

    @Operation(summary = "Inserção de novos Resorts / hoteis", description = "Inserção de novos resorts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    @PostMapping(value = "/insert")
    public ResponseEntity<HotelDTO> insertResort(@RequestBody HotelDTO data) {

        HotelDTO entity = hotelService.insert(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @GetMapping("/{id}/available/rooms")
    @Operation(summary = "busca de todos os quartos disponíveis", description = "passando o id do Resort irá trazer todos os quartos disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisnição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    public ResponseEntity<HotelDTO> findAllRooms(@PathVariable Long id) {
        HotelDTO list = hotelService.searchAllRooms(id);
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Reserva de quartos",description = "Usuário disponível para teste: angela@gmail.com, Resort disponível para teste ID: 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisnição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @PostMapping("/{id}/reserve")
    public ResponseEntity<ResponseRentedRoom> reserveRooms(@PathVariable Long id, @RequestBody ReserveRoomsRequestDTO requestDTO) {

        ResponseRentedRoom data = hotelService.reserveRoom(requestDTO, id, requestDTO);
        return ResponseEntity.ok().body(data);
    }


    @Operation(summary = "Exclusão do quarto ",description = "Passando o ID do Resort exemplo disponível: 1,  e passando o numero do quarto que contem ou nao um usuário ele será exluido e resetado automaticamente. id do quarto que contem usuários 1 e 2, quartos vagos 3 ao 10")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisnição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @DeleteMapping("/{resortId}/Room/{roomId}")
    public ResponseEntity<Void> restoreRoom(@PathVariable Long resortId, @PathVariable Long roomId) {
        hotelService.deleteRoom(resortId, roomId);

        return ResponseEntity.ok().build();
    }

}
