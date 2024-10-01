package com.lanaVitor.Reservas.com.controllers;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.dtos.records.LoginResponseDTO;
import com.lanaVitor.Reservas.com.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Validated
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://reservas-44eh.onrender.com", "https://reservas-five.vercel.app"})
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "cadastro de novos usuários", description = "cadastro de novos usuários")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponível, Unprocessable Entity "),
            @ApiResponse(responseCode = "400", description = "Recurso Indisponível, Bad Request")})
    public ResponseEntity<UserRegistrationDTO> register(@RequestBody @Valid UserDTO data) {

        UserRegistrationDTO dto = userService.registerUser(data);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // usar o chahe no login impede a ida no banco de dados, se voce precisar ir
    // ate o banco para verificar se o usuario existe, nao use o cache
    //@Cacheable("cache")
    @Transactional
    @Operation(summary = "Login de usuários cadastrados", description = "usuários cadastrados exemplo: angela@gmail.com")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponível, Unprocessable Entity "),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponível, Not Found")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data) {
        String response = userService.validateLogin(data);
        return ResponseEntity.ok().body(new LoginResponseDTO(response));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "busca por usuario atraves do ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponível, Not Found")})
    @GetMapping("/find/{id}")
    public ResponseEntity<UserRegistrationDTO> findById(@PathVariable Long id) {
        var entity = userService.findById(id);
        return ResponseEntity.ok().body(entity);
    }

    @Cacheable("cache")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "busca por todos usuário ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponível, Not Found")})
    @GetMapping("/findAll")
    public ResponseEntity<List<ListUsersDTO>> findAll() {
        var list = userService.findAllUsers();
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/update/{id}")
    @Operation(summary = "Update de usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Recurso Indisponível, Not Found")})
    public ResponseEntity<UserUpdateResponse> userUpdate(@RequestBody UpdateUserDTO updateUserDTO, @PathVariable Long id) {

        UserUpdateResponse entity = userService.updateUser(updateUserDTO, id);
        return ResponseEntity.ok().body(entity);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "deleção de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A requisição foi executada com sucesso."),
            @ApiResponse(responseCode = "422", description = "Recurso Indisponível, Unprocessable Entity ")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
