package com.spring.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.repository.PedidoRepository;
import com.spring.ecommerce.service.interfaces.ICrudService;

@Service
public class PedidoService implements ICrudService<Pedido> {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public PedidoService(PedidoRepository pedidoRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido getById(int id) {
        return pedidoRepository.findById(id).get();
    }

    @Override
    public Pedido save(Pedido pedido) {
        Integer ultimoPedidoId = pedidoRepository.findLastId();
        pedido.setId(ultimoPedidoId == null ? 1 : ultimoPedidoId++);
        pedido.setDataHora(LocalDateTime.now());
        
        if (pedido.isFinalizado()) {
            return pedidoRepository.save(pedido);
        }

        try {
            String pedidoJson = objectMapper.writeValueAsString(pedido);
            redisTemplate.opsForValue().set(String.format("PEDIDO%d", pedido.getId()), pedidoJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        return pedido;
    }

    @Override
    public Pedido update(int id, Pedido pedido) {
        Pedido pedidoAtualizado = pedidoRepository.findById(id).get();
        if (pedidoAtualizado.isFinalizado()) {
            try {
                throw new Exception("Pedido já foi finalizado, não pode ser editado.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pedidoAtualizado.setDataHora(LocalDateTime.now());
        pedidoAtualizado.setCliente(pedido.getCliente());
        pedidoAtualizado.setItens(pedido.getItens());

        if (pedido.isFinalizado()) {
            redisTemplate.opsForValue().set(String.format("PEDIDO%d", id), pedidoAtualizado);
            return pedidoAtualizado;
        }
        
        return pedidoRepository.save(pedidoAtualizado);
    }

    @Override
    public boolean delete(int id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
