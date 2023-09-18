package com.spring.ecommerce.service;

import com.spring.ecommerce.model.Produto;
import com.spring.ecommerce.service.interfaces.ICrudService;

public abstract class ProdutoBase implements ICrudService<Produto> {

    @Override
    public abstract Produto getById(int id);

    @Override
    public abstract Produto save(Produto obj);

    @Override
    public abstract Produto update(int id, Produto obj);

    @Override
    public abstract boolean delete(int id);
    
}
