package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.ListUsersDTO;
import com.lanaVitor.Reservas.com.dtos.UpdateUserDTO;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.dtos.UserRegistrationDTO;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.ExistsUserException;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import com.lanaVitor.Reservas.com.services.exception.noExistsUserException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private EmailService emailService;
    private final UserRepository repository;


    @Autowired
    public UserService(EmailService emailService, UserRepository repository) {
        this.emailService = emailService;
        this.repository = repository;
    }

    @Transactional
    public UserRegistrationDTO registerUser(UserDTO data) {

        var user = repository.findUserByEmail(data.getEmail());
        if (user != null) new ExistsUserException("Email já existente!");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        User entity = new User(data.getName(), data.getEmail(), encryptedPassword, data.getRole());

        entity = repository.save(entity);

        // Envio de e-mail de confirmação
        emailService.sendEmailText(entity.getEmail(), "Novo usuário cadastrado", "Obrigado por efetuar o cadastro em nossa plataforma!");
        return new UserRegistrationDTO(entity);
    }

    @Transactional()
    public UserRegistrationDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new noExistsUserException("Usuário não existente"));
        return new UserRegistrationDTO(user);
    }

    @Transactional
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO, Long id) {

        User entity = repository.findById(id).orElseThrow(() -> new ExistsUserException("Email já cadastrado!"));

        entity.setName(updateUserDTO.getName());
        entity.setEmail(updateUserDTO.getEmail());
        entity.setPassword(updateUserDTO.getPassword());

        User saveUser = repository.save(entity);

        return new UpdateUserDTO(saveUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(Long id) {

        var entity = repository.findById(id);
        if (entity.isEmpty()) throw new ResourceNotFoundException("Usuário não encontrado!");
        else repository.deleteById(entity.get().getId());
    }

    public List<ListUsersDTO> findAllUsers() {
        var list = repository.findAll();
        if (!list.isEmpty()) {
            return ConvertToUserDTO(list);
        } else {
            new ResourceNotFoundException("Recurso nao encontrado");
        }
        return null;
    }

    public List<ListUsersDTO> ConvertToUserDTO(List<User> listUser) {
        List<ListUsersDTO> listDTO = new ArrayList<>();

        for (User list : listUser) {
            ListUsersDTO usersDTO = new ListUsersDTO();
            usersDTO.setId(list.getId());
            usersDTO.setName(list.getName());
            usersDTO.setEmail(list.getEmail());
            usersDTO.setRole(list.getRole());
            listDTO.add(usersDTO);
        }
        return listDTO;
    }
}
