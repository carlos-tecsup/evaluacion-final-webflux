package com.mitocode.enums;

public enum RoleEnum {
    ROLE_USER("1"),
    ROLE_ADMIN("2");

    private final String id;

    RoleEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Método auxiliar para buscar por nombre
    public static RoleEnum fromName(String roleName) {
        try {
            return RoleEnum.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + roleName);
        }
    }
}
