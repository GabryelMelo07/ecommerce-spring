package com.spring.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Item;
import com.spring.ecommerce.repository.ItemRepository;
import com.spring.ecommerce.service.interfaces.ICrudService;

@Service
public class ItemService implements ICrudService<Item> {

    @Autowired
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item getById(int id) {
        return itemRepository.findById(id).get();
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(int id, Item item) {
        Item itemAtualizado = itemRepository.findById(id).get();
        itemAtualizado.setQuantidade(item.getQuantidade());
        itemAtualizado.setValor(item.getValor());
        return itemRepository.save(itemAtualizado);
    }

    @Override
    public boolean delete(int id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
