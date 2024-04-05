package com.lanaVitor.Reservas.com.services;

import com.lanaVitor.Reservas.com.dtos.HotelDTO;
import com.lanaVitor.Reservas.com.dtos.RoomsDTO;
import com.lanaVitor.Reservas.com.entities.Hotel;
import com.lanaVitor.Reservas.com.entities.Rooms;
import com.lanaVitor.Reservas.com.repositories.HotelRepository;
import com.lanaVitor.Reservas.com.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class RoomsService {
    private final RoomsRepository repository;


    @Autowired
    public RoomsService(RoomsRepository repository) {
        this.repository = repository;
    }


}
