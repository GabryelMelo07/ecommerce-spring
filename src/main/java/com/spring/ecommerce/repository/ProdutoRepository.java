package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
}
