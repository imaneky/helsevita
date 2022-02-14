package com.urjc.es.helsevita.Services;

import com.urjc.es.helsevita.Entities.HealthPersonnel;
import com.urjc.es.helsevita.Entities.Patient;
import com.urjc.es.helsevita.Repositories.HealthPersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HealthPersonnelService {

    @Autowired
    HealthPersonnelRepository healthPersonnelRepository;    

    @Autowired
    AppointmentService appointmentService;

    
    public HealthPersonnel addHealthPersonnel(HealthPersonnel healthPersonnel){
        return healthPersonnelRepository.save(healthPersonnel);
    }

    public boolean exists(Integer id){
        return healthPersonnelRepository.findById(id).isPresent();
    }

    public void delete(Integer id){
        HealthPersonnel temp;
        Optional<HealthPersonnel> tempOptional = healthPersonnelRepository.findById(id);
            if (tempOptional.isPresent()) {
                temp = tempOptional.get();
                healthPersonnelRepository.delete(temp);
            }
    }

    public HealthPersonnel returnHealthPersonnel(Integer id){
        HealthPersonnel temp;
        Optional<HealthPersonnel> tempOptional = healthPersonnelRepository.findById(id);
        if (tempOptional.isPresent()){
            temp = tempOptional.get();
            return temp;
        }
        return null;
    }

    public List<HealthPersonnel> returnAllHealthPersonnels(){
        return healthPersonnelRepository.findAll();
    }

    public List<HealthPersonnel> search(String input){
        return healthPersonnelRepository.findHealthPersonnelByNameContainsIgnoreCaseOrSurname1ContainsIgnoreCaseOrSurname2ContainsIgnoreCaseOrEmailContainsIgnoreCase(input, input, input, input);
    }

    public List<HealthPersonnel> searchByAge(String input){
        if (input.equals("")){
            return Collections.emptyList();
        }else{
            return healthPersonnelRepository.findByAge(Integer.parseInt(input));
        }

    }

    public HealthPersonnel returnHealthPersonnelByUsername(String username){
        var temp = healthPersonnelRepository.findHealthPersonnelByUsername(username);
        return temp.orElse(null);
    }

    public List<HealthPersonnel> returnHealthPersonnelsByPatient(List<Patient> lista){
        return healthPersonnelRepository.findHealthPersonnelsByPatientsIn(lista);
    }

    public List <Patient> addPatientToHealthPersonnel(Integer id, Patient patient){
        var temp = healthPersonnelRepository.findById(id).orElse(null);
        List <Patient> list = temp.getPatients();
        list.add(patient);
        healthPersonnelRepository.save(temp);
        return list;
    }
}
