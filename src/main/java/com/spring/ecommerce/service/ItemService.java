package com.spring.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommerce.model.Item;
import com.spring.ecommerce.repository.ItemRepository;
import com.spring.ecommerce.service.interfaces.ICrudService;

@Service
public class ItemService implements ICrudService<Item> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ItemService(ItemRepository itemRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.itemRepository = itemRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
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
        item.setValor(item.getProduto().getValor().multiply(BigDecimal.valueOf(item.getQuantidade())));

        if (item.getPedido().isFinalizado()) {
            return itemRepository.save(item);
        }
        
        try {
            String itemJson = objectMapper.writeValueAsString(item);
            redisTemplate.opsForValue().set(String.format("ITEM%d", item.getId()), itemJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return item;
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
