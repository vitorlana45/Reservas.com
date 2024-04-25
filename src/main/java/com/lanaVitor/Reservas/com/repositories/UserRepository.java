package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
