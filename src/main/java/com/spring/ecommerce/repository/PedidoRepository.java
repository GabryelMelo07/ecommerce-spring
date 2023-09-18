package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
}
