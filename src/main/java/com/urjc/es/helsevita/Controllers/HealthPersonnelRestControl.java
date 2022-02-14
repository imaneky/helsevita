package com.urjc.es.helsevita.Controllers;
import com.urjc.es.helsevita.Entities.*;

import com.urjc.es.helsevita.Services.HealthPersonnelService;
import com.urjc.es.helsevita.Services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
public class HealthPersonnelRestControl {
    @Autowired
    HealthPersonnelService healthPersonnelService;

    @Autowired
    PatientService patientService;

    @PostMapping("/api/healthPersonnels")
    @ResponseStatus(HttpStatus.CREATED)
    public HealthPersonnel newHealthPersonnel(@RequestBody HealthPersonnel healthPersonnel){
        healthPersonnel.setPassword(new BCryptPasswordEncoder().encode(healthPersonnel.getPassword()));
        return healthPersonnelService.addHealthPersonnel(healthPersonnel);
    }

    @PutMapping("/api/healthPersonnels/{id}")
    public ResponseEntity<HealthPersonnel> updateHealthPersonnel(@PathVariable Integer id, @RequestBody HealthPersonnel healthPersonnel){
        if (healthPersonnelService.exists(id)){
            return new ResponseEntity<>(healthPersonnelService.addHealthPersonnel(healthPersonnel), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/healthPersonnels/{id}")
    public ResponseEntity<HealthPersonnel> deleteHealthPersonnel(@PathVariable Integer id){
        if (healthPersonnelService.exists(id)){
            healthPersonnelService.delete(id);
            return  new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Get one specified user
    @GetMapping("/api/healthPersonnels/{id}")
    public ResponseEntity<HealthPersonnel> getSingleHealthPersonnel(@PathVariable Integer id){
        if (healthPersonnelService.exists(id)){
            return new ResponseEntity<>(healthPersonnelService.returnHealthPersonnel(id),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //Get All Users
    @GetMapping("/api/healthPersonnels")
    @ResponseStatus(HttpStatus.OK)
    public Collection<HealthPersonnel> returnAll(){
        return healthPersonnelService.returnAllHealthPersonnels();
    }


    @PostMapping("/api/setNewPatient")
    public String setNewPatient(@RequestBody Integer idHealthPersonnel, @RequestBody Integer idPatient){
        healthPersonnelService.addPatientToHealthPersonnel(idHealthPersonnel, patientService.returnPatient(idPatient));
        return "exito";
    }
}
