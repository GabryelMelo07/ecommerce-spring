package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.ecommerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    @Query(value = "SELECT max(id) FROM pedido", nativeQuery = true)
    Integer findLastId();
    
}
