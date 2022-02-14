package com.urjc.es.helsevita.Services;

import com.urjc.es.helsevita.Entities.Admin;
import com.urjc.es.helsevita.Repositories.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;
    
    public Admin addAdmin(Admin admin){
        return adminRepository.save(admin);
        }
}

