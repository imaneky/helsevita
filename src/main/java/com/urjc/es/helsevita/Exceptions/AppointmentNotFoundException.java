package com.urjc.es.helsevita.Exceptions;

public class AppointmentNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AppointmentNotFoundException() {
        super("No hay citas");
    }

}

