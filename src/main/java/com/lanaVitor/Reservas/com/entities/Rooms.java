package com.lanaVitor.Reservas.com.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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

    @Setter
    private Integer roomsNumber;

    @Setter
    private Date checkIn;
    @Setter
    private Date checkOut;

    @Getter
    @Setter
    private boolean rented;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    @Getter
    private Hotel hotel;

    @ManyToOne
    @JsonIgnore
    @Setter
    private User user;

    public boolean isAvailable() {
        return rented;
    }

}
