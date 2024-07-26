package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.dtos.records.LoginResponseDTO;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.infra.security.TokenService;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public UserController(UserService service, UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.service = service;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }


    @PostMapping("/register")
    @Operation(summary = "cadastro de novos usuários", description = "cadastro de novos usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponivel, Unprocessable Entity "),
            @ApiResponse(responseCode = "400", description = "Recurso Indisponivel, Bad Request")})
    public ResponseEntity<UserRegistrationDTO> register(@RequestBody @Valid UserDTO data) {

        UserRegistrationDTO dto = service.registerUser(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Cacheable("cache")
    @Operation(summary = "Login de usuários cadastrados", description = "usuarios cadastrador exemplo: angela@gmail.com")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponivel, Unprocessable Entity "),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        service.saveLogin(data);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "busca por usuario atraves do ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    @GetMapping("/find/{id}")
    public ResponseEntity<UserRegistrationDTO> findById(@PathVariable Long id) {
        var entity = service.findById(id);
        return ResponseEntity.ok().body(entity);
    }

    @Cacheable("cache")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "busca por todos usuario ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    @GetMapping("/findAll")
    public ResponseEntity<List<ListUsersDTO>> findAll() {
        var list = service.findAllUsers();
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/update/{id}")
    @Operation(summary = "Update de usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponivel, Not Found")})
    public ResponseEntity<UserUpdateResponse> userUpdate(@RequestBody UpdateUserDTO updateUserDTO, @PathVariable Long id) {

        UserUpdateResponse entity = service.updateUser(updateUserDTO, id);
        return ResponseEntity.ok().body(entity);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "deleção de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com secusso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponivel, Unprocessable Entity ")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
