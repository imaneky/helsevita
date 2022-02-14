package com.urjc.es.helsevita.Repositories;

import com.urjc.es.helsevita.Entities.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Admin findByUsername(String username);
}
