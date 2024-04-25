package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
