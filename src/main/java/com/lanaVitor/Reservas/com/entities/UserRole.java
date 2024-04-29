package com.lanaVitor.Reservas.com.entities;

import lombok.Setter;

public enum UserRole{
    ADMIN("admin"),
    USER("user");

    private static final long serialVersionUID = 1L;

    @Setter
    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
