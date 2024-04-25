package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import com.lanaVitor.Reservas.com.dtos.UserRegistrationDTO;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final RoomsRepository roomsRepository;

    @Autowired
    public UserController(UserService service, RoomsRepository roomsRepository) {
        this.service = service;
        this.roomsRepository = roomsRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "cadastro de novos usuários", description = "cadastro de novos usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponivel, Unprocessable Entity "),
            @ApiResponse(responseCode = "400", description = "Recurso Indisponivel, Bad Request")})
    public ResponseEntity<UserRegistrationDTO> register(@RequestBody @Valid UserDTO data) {
        UserRegistrationDTO dto = service.registerUser(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    @Operation(summary = "Logind de usuários cadastrados", description = "usuarios cadastrador exemplo: angela@gmail.com")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponivel, Unprocessable Entity "),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginDTO data) {
        boolean dto = service.login(data);
        if (dto) return ResponseEntity.ok().build();
        else return ResponseEntity.unprocessableEntity().build();

    }
}
