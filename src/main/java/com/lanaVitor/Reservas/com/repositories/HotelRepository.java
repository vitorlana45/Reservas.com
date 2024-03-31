package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
