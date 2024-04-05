package com.lanaVitor.Reservas.com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private String location;
    @Setter
    private String description;
    @Setter
    private String status;

    @Getter
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Rooms> listRooms;

    @ManyToMany
    @JoinTable(
            name = "tb_hotel_user",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

    public Hotel(Long id, String name, String location, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public void addRooms(Rooms entity) {
        listRooms.add(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
