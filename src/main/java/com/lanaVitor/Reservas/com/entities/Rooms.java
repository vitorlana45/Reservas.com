package com.lanaVitor.Reservas.com.entities;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public boolean isAvailable() {
        return !rented;
    }
}