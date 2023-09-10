package com.spring.ecommerce.service.interfaces;

import java.util.List;

public interface ICrudService<T> {
    List<T> getAll();
    T getById(int id);
    T save(T obj);
    T update(int id, T obj);
    boolean delete(int id);
}
