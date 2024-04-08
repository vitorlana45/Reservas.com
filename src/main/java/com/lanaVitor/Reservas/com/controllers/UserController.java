package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import com.lanaVitor.Reservas.com.dtos.UserRegistrationDTO;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<UserRegistrationDTO> register(@RequestBody UserDTO data) {
        UserRegistrationDTO dto = service.registerUser(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(data.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginDTO data) {
        boolean dto = service.login(data);
        if (dto == true) return ResponseEntity.ok().build();
        else return ResponseEntity.unprocessableEntity().build();

    }
}
