package com.urjc.es.helsevita.Services;

import com.urjc.es.helsevita.Entities.Appointment;
import com.urjc.es.helsevita.Entities.HealthPersonnel;
import com.urjc.es.helsevita.Entities.Patient;
import com.urjc.es.helsevita.Repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    
    public Appointment addAppointment(Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    public boolean exists(Integer id){
        var temp = appointmentRepository.findById(id);
        return temp.isPresent();
    }


    public void delete(Integer id){
        Appointment temp;
        Optional<Appointment> tempOptional = appointmentRepository.findById(id);
            if (tempOptional.isPresent()) {
                temp = tempOptional.get();
                appointmentRepository.delete(temp);
            }
    }

    public Appointment returnAppointment(Integer id){
        Appointment temp;
        Optional<Appointment> tempOptional = appointmentRepository.findById(id);
        if (tempOptional.isPresent()){
            temp = tempOptional.get();
            return temp;
        }
        return null;
    }


    public Collection<Appointment> returnAllAppointmentsOfPatient(Patient patient){
        return appointmentRepository.findAppointmentsByPatientId(patient.getId());
    }

    public List <HealthPersonnel> takenHealthPersonnel(Integer year, Integer month, Integer day, Integer hour, Integer minute){
        List <Appointment> temp = appointmentRepository.findAppointmentByYearAndMonthAndDayAndHourAndMinute(year, month, day, hour, minute);
        List <HealthPersonnel> temp2 = new ArrayList<>();
        for(Appointment entry : temp){
            temp2.add(entry.getHealthPersonnel());
        }
        return temp2;
    }
    public Collection<Appointment> returnAllAppointments(){
        return appointmentRepository.findAll();
    }
}
