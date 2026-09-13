package com.livrotech.entity;

public enum AppUserRole {
    ADMIN,
    USER;

    public static AppUserRole from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        String normalized = value.trim();
        for (AppUserRole role : values()) {
            if (role.name().equalsIgnoreCase(normalized)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Unsupported role: " + value);
    }
}
