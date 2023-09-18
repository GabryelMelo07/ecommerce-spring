package com.spring.ecommerce.service;

import java.util.List;

import com.spring.ecommerce.model.Cliente;
import com.spring.ecommerce.service.interfaces.ICrudService;

abstract class ClienteBase implements ICrudService<Cliente> {

    @Override
    public abstract List<Cliente> getAll();

    @Override
    public abstract Cliente getById(int id);

    @Override
    public abstract Cliente save(Cliente cliente);

    @Override
    public abstract Cliente update(int id, Cliente cliente);

    @Override
    public abstract boolean delete(int id);

}
