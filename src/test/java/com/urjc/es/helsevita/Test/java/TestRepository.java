package com.urjc.es.helsevita.Test.java;

import com.urjc.es.helsevita.Entities.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


import java.util.Optional;


@SpringBootConfiguration
@SpringBootTest
@ComponentScan("com.urjc.es.helsevita")
class TestRepository {
    @Autowired
    com.urjc.es.helsevita.Services.PatientService patientService;

    @Test
    public void savePatientCheck() {
        boolean funciona;
        Patient patient = new Patient("davide", "Davide1234", "davide@urjc.es",
                "1234567E", "Davide", "Apellido1", "Apellido2", 30);
        patientService.addPatient(patient);
        Optional<Patient> davide = Optional.ofNullable(patientService.returnPatientByUsername("davide"));
        funciona = davide.isPresent();
        Assertions.assertTrue(funciona);
    }
}
