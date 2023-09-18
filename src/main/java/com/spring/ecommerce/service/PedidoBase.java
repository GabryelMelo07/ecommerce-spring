package com.spring.ecommerce.service;

import java.util.List;

import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.service.interfaces.ICrudService;

abstract class PedidoBase implements ICrudService<Pedido> {

    @Override
    public abstract List<Pedido> getAll();

    @Override
    public abstract Pedido getById(int id);

    @Override
    public abstract Pedido save(Pedido obj);

    @Override
    public Pedido update(int id, Pedido obj) {
        return new Pedido();
    };

    @Override
    public abstract boolean delete(int id);
    
}
