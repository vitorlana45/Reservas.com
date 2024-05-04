package com.lanaVitor.Reservas.com.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "tb_rooms")
@Table(name = "tb_rooms")
public class Rooms implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer roomsNumber;

    private Date checkIn;
    private Date checkOut;

    @Getter
    private boolean rented;

    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    @Getter
    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    @JsonIgnore
    private User user;


    public boolean isAvailable() {
        return !rented;
    }

}
