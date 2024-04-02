package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.LoginDTO;
import com.lanaVitor.Reservas.com.dtos.UserDTO;
import com.lanaVitor.Reservas.com.entities.Login;
import com.lanaVitor.Reservas.com.entities.User;
import com.lanaVitor.Reservas.com.repositories.LoginRepository;
import com.lanaVitor.Reservas.com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private EmailService emailService;
    private final UserRepository repository;

    private LoginRepository loginRepository;

    @Autowired
    public UserService(UserRepository repository, EmailService emailService, LoginRepository loginRepository) {
        this.emailService = emailService;
        this.repository = repository;
        this.loginRepository = loginRepository;

    }


    public UserDTO insert(UserDTO data) {
        User entity = new User(data);
        entity = repository.save(entity);
        emailService.sendEmailText(entity.getEmail(), "Novo usuário cadastro", "Obrigado por efetuar o cadastro em nossa plataforma!");
        return new UserDTO(entity);
    }

    public boolean login(LoginDTO data) {
        if (loginValidation(data) == true) return true;
        else return false;
    }

    private boolean loginValidation(LoginDTO dto) {
        User user = repository.findByEmail(dto.getEmail());
        if (user != null && user.getPassword().equals(dto.getPassword())){
            loginRepository.save(new Login(user));
            return true;
        }
        else return false;
    }
}
