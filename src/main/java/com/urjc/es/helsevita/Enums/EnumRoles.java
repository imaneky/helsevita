package com.urjc.es.helsevita.Enums;

import java.util.List;
import java.util.Random;

public enum EnumRoles {
    CARDIOLOGO, PEDIATRA, DERMATOLOGO, TRAUMATOLOGO, GINECOLOGO, 
    ALERGOLOGO, ENDOCRINO, ENFERMERO, NEUROLOGO, OFTALMOLOGO, 
    ONCOLOGO, PSIQUIATRA, CIRUJANO, ODONTOLOGO, UROLOGO;    

    
    public static final List<EnumRoles> rolValues = List.of(values());
    private static final int SIZE = rolValues.size();
    private static final Random RANDOM = new Random(); 
    public static EnumRoles randomRol() { return rolValues.get(RANDOM.nextInt(SIZE)); }


    @Override
    public String toString() {
        return this.name();
    }
}
