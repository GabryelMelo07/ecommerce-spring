package com.spring.ecommerce.service;

import java.util.List;

import com.spring.ecommerce.model.Item;
import com.spring.ecommerce.service.interfaces.ICrudService;

abstract class ItemBase implements ICrudService<Item> {

    @Override
    public abstract List<Item> getAll();

    @Override
    public abstract Item getById(int id);

    @Override
    public abstract Item save(Item item);

    @Override
    public abstract Item update(int id, Item item);

    @Override
    public abstract boolean delete(int id);
    
}
