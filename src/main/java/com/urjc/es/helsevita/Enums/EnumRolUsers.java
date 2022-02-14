package com.urjc.es.helsevita.Enums;



public enum EnumRolUsers {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_PATIENT("ROLE_PATIENT"),
    ROLE_HEALTHPERSONNEL("ROLE_HEALTHPERSONNEL"),
    ROLE_GUEST("ROLE_GUEST");

    private final String rol;

    EnumRolUsers(String str) {
        this.rol = str;
    }


    @Override
    public String toString() {
        return this.rol;
    }
}
