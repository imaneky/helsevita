package com.urjc.es.helsevita.Exceptions;

import com.urjc.es.helsevita.Entities.Appointment;

import javax.persistence.Transient;

public class AppointmentAlreadyExistsException extends RuntimeException{
    @Transient
    final Appointment appointment;
    private static final long serialVersionUID = 1L;

    public AppointmentAlreadyExistsException(Appointment appointment){
        super("Appointment " + appointment + "already exists");
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

}
