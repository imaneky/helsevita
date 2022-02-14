package com.urjc.es.helsevita.Repositories;

import com.urjc.es.helsevita.Entities.HealthPersonnel;
import com.urjc.es.helsevita.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository

public interface PatientRepository extends JpaRepository<Patient,Integer>, JpaSpecificationExecutor<Patient> {

    Optional<Patient> findByUsername(String username);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByDni(String dni);

    Collection<Patient> findPatientByNameContainsIgnoreCaseOrSurname1ContainsIgnoreCaseOrSurname2ContainsIgnoreCaseOrEmailContainsIgnoreCase(String input, String input1, String input2, String input3);

    List<Patient> findPatientByAge(Integer age);

    List<Patient> findPatientByHealthPersonnelList(HealthPersonnel healthPersonnel);
}
