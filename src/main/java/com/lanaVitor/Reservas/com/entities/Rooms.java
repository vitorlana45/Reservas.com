package com.lanaVitor.Reservas.com.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "tb_rooms")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer roomsNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Getter
    private boolean rented;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public boolean isAvailable() {
        return !rented;
    }
}