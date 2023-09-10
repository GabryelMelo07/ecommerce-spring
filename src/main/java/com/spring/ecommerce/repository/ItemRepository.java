package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    
}
