package com.urjc.es.helsevita.Repositories;
import java.util.List;
import java.util.Optional;

import com.urjc.es.helsevita.Entities.Appointment;
import com.urjc.es.helsevita.Entities.HealthPersonnel;
import com.urjc.es.helsevita.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findAppointmentByYearAndMonthAndDayAndHourAndMinute(Integer year, Integer month, Integer day,Integer hour, Integer minute);
    Optional<Appointment> findAppointmentsByPatient(Patient patient);
    public Optional<Appointment> findAppointmentsByHealthPersonnel(HealthPersonnel healthPersonnel);
    public List<Appointment> findAppointmentsByPatientId(Integer patientId);



}
