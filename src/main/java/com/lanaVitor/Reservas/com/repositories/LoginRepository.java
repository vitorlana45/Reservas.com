package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}
