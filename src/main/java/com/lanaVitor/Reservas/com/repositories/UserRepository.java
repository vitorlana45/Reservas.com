package com.lanaVitor.Reservas.com.repositories;

import com.lanaVitor.Reservas.com.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String username);

    @Query("SELECT u FROM tb_users u WHERE u.email = :email")
    User findUserByEmail(@Param("email") String email);
}
