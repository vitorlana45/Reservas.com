package com.lanaVitor.Reservas.com.entities;


import lombok.Getter;

@Getter

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private static final long serialVersionUID = 1L;

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}