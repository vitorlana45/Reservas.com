package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.Hotel;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // opção para se usar o EntityGraph ele faz um join com a tabela listRooms
    // ssim posso evitar o lazy loading e fazer a busca de todos os quartos do hotel
    @EntityGraph(attributePaths = {"listRooms"})
    @Query("SELECT h FROM tb_hotel h WHERE h.id = :id")
    Hotel searchHotelById(Long id);


    @Query(nativeQuery = true, value = """
        SELECT * FROM (
            SELECT DISTINCT tb_hotel.id, tb_hotel.name, tb_hotel.location, tb_hotel.description, tb_hotel.status
            FROM tb_hotel
            INNER JOIN tb_rooms ON tb_rooms.hotel_id = tb_hotel.id
            WHERE (:hotelId IS NULL OR tb_hotel.id = :hotelId)
        ) AS tb_result
        """, countQuery = """
        SELECT COUNT(*) FROM (
            SELECT DISTINCT tb_hotel.id, tb_hotel.name, tb_hotel.location, tb_hotel.description, tb_hotel.status
            FROM tb_hotel
            INNER JOIN tb_rooms ON tb_rooms.hotel_id = tb_hotel.id
            WHERE (:hotelId IS NULL OR tb_hotel.id = :hotelId)
        ) AS tb_result
""")
    Page<Hotel> searchAllRooms(Long hotelId, Pageable pageable);

}
