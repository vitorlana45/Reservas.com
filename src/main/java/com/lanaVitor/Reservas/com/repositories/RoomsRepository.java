package com.lanaVitor.Reservas.com.repositories;


import com.lanaVitor.Reservas.com.entities.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {

}
