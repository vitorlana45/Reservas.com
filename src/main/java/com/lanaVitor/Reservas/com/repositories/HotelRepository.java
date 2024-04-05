package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
