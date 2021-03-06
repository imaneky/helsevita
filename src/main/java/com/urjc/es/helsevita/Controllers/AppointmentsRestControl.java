package com.urjc.es.helsevita.Controllers;
import com.urjc.es.helsevita.Entities.Appointment;
import com.urjc.es.helsevita.Services.AppointmentService;
import com.urjc.es.helsevita.Services.HealthPersonnelService;
import com.urjc.es.helsevita.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
    public class AppointmentsRestControl {

        @Autowired
        AppointmentService appointmentService;

        @Autowired
        PatientService patientService;

        @Autowired
        HealthPersonnelService healthPersonnelService;

        @PostMapping("/api/appointments")
        @ResponseStatus(HttpStatus.CREATED)
        public Appointment newAppointment(@RequestBody Appointment appointment) {
            return appointmentService.addAppointment(appointment);
        }

        @PutMapping("/api/appointments/{id}")
        public ResponseEntity<Appointment> updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
            if (appointmentService.exists(id)) {
                return new ResponseEntity<>(appointmentService.addAppointment(appointment), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @DeleteMapping("/api/appointments/{id}")
        public ResponseEntity<Appointment> deleteAppointment(@PathVariable Integer id) {
            if (appointmentService.exists(id)) {
                appointmentService.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //Get one specified appointment
        @GetMapping("/api/appointments/{id}")
        public ResponseEntity<Appointment> getSingleAppointment(@PathVariable Integer id) {
            if (appointmentService.exists(id)) {
                return new ResponseEntity<>(appointmentService.returnAppointment(id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }


        //Get All Users
        @GetMapping("/api/appointments")
        @ResponseStatus(HttpStatus.OK)
        public Collection<Appointment> returnAll() {
            return appointmentService.returnAllAppointments();
        }

        @PatchMapping("/api/appointments/{id}")
        public ResponseEntity<Appointment> patchAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
            Appointment persistentAppointment = new Appointment();
            if (appointmentService.exists(id)) {
                //Get actual Appointment with that ID
                Appointment temp = appointmentService.returnAppointment(id);

                if (persistentAppointment.getDay() != null)
                    temp.setDay(persistentAppointment.getDay());
                if (persistentAppointment.getHour() != null)
                    temp.setHour(persistentAppointment.getHour());
                if (persistentAppointment.getMonth() != null)
                    temp.setMonth(persistentAppointment.getMonth());
                if (persistentAppointment.getYear() != null)
                    temp.setYear(persistentAppointment.getYear());

                return new ResponseEntity<>(appointmentService.addAppointment(temp), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

