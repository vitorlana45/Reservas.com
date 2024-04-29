package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import com.lanaVitor.Reservas.com.dtos.UpdateUserDTO;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.dtos.UserRegistrationDTO;
import com.lanaVitor.Reservas.com.entities.Login;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.LoginRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.ExistingUserException;
import com.lanaVitor.Reservas.com.services.exception.NullEntityException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private EmailService emailService;
    private final UserRepository repository;

    private LoginRepository loginRepository;

    private RoomsRepository roomsRepository;

    @Autowired
    public UserService(UserRepository repository, EmailService emailService, LoginRepository loginRepository, RoomsRepository roomsRepository) {
        this.emailService = emailService;
        this.repository = repository;
        this.loginRepository = loginRepository;
        this.roomsRepository = roomsRepository;
    }

    @Transactional
    public UserRegistrationDTO registerUser(UserDTO data) {

        User newUser = new User(data);
        newUser = repository.save(newUser);

        // Envio de e-mail de confirmação
        emailService.sendEmailText(newUser.getEmail(), "Novo usuário cadastrado", "Obrigado por efetuar o cadastro em nossa plataforma!");

        return new UserRegistrationDTO(newUser);
    }
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO, Long id) {

        User entity = repository.findById(id).orElseThrow(() -> new NullEntityException("Email já cadastrado!"));

        entity.setName(updateUserDTO.getName());
        entity.setEmail(updateUserDTO.getEmail());
        entity.setPassword(updateUserDTO.getPassword());

        User saveUser = repository.save(entity);

        return new UpdateUserDTO(saveUser);
    }

    private boolean loginValidation(LoginDTO dto) {
        User user = repository.findUserByEmail(dto.getEmail());
        if (user != null && user.getPassword().equals(dto.getPassword())) {
            loginRepository.save(new Login(user));
            return true;
        } else return false;
    }
}
