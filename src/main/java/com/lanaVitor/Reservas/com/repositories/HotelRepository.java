package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.Hotel;
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

}
