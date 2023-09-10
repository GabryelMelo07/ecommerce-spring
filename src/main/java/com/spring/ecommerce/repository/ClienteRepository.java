package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
}
