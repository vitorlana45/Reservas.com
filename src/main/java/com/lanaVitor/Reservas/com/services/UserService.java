package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.*;
import com.lanaVitor.Reservas.com.entities.Login;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.entities.UserRole;
import com.lanaVitor.Reservas.com.repositories.LoginRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import com.lanaVitor.Reservas.com.services.exception.ExistsUserException;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import com.lanaVitor.Reservas.com.services.exception.noExistsUserException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private EmailService emailService;
    private final UserRepository repository;

    private final LoginRepository loginRepository;


    @Autowired
    public UserService(EmailService emailService, UserRepository repository,LoginRepository loginRepository) {
        this.emailService = emailService;
        this.repository = repository;
        this.loginRepository =loginRepository;
    }

    @Transactional
    public UserRegistrationDTO registerUser(UserDTO data) {

        var user = repository.findUserByEmail(data.getEmail());
        if (user != null) new ExistsUserException("Email já existente!");

        if (Optional.ofNullable(data.getRole()).map(Enum::name).orElse("").isEmpty()) {
            data.setRole(UserRole.USER);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        User entity = new User(data.getName(), data.getEmail(), encryptedPassword, data.getRole());

        entity = repository.save(entity);

        // Envio de e-mail de confirmação
        emailService.sendEmailText(entity.getEmail(), "Novo usuário cadastrado", "Obrigado por efetuar o cadastro em nossa plataforma!");
        return new UserRegistrationDTO(entity);
    }

    public void saveLogin(LoginDTO data) {
        loginRepository.save(new Login(data));
    }

    @Transactional()
    public UserRegistrationDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new noExistsUserException("Usuário não existente"));
        return new UserRegistrationDTO(user);
    }

    @Transactional
    public UserUpdateResponse updateUser(UpdateUserDTO updateUserDTO, Long id) {

        User entity = repository.findById(id).orElseThrow(() -> new ExistsUserException("Usuário não encontrado"));

        entity.setName(updateUserDTO.getName());
        entity.setEmail(updateUserDTO.getEmail());
        String encryptedPassword = new BCryptPasswordEncoder().encode(updateUserDTO.getPassword());
        entity.setPassword(encryptedPassword);

        User saveUser = repository.save(entity);

        return new UserUpdateResponse(saveUser);
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
