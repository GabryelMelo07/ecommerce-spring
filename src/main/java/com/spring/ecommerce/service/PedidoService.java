package com.spring.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommerce.DTO.PedidoDTO;
import com.spring.ecommerce.model.Cliente;
import com.spring.ecommerce.model.Item;
import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.repository.ClienteRepository;
import com.spring.ecommerce.repository.ItemRepository;
import com.spring.ecommerce.repository.PedidoRepository;

@Service
public class PedidoService extends PedidoBase {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public PedidoService(PedidoRepository pedidoRepository, ItemRepository itemRepository, ClienteRepository clienteRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.itemRepository = itemRepository;
        this.clienteRepository = clienteRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido getById(int id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido inexistente."));
    }

    @Override
    public Pedido save(Pedido pedido) {
        int clienteId = pedido.getCliente().getId();

        String pedidoFromJson = (String) redisTemplate.opsForValue().get(String.format("PEDIDO%d", clienteId));

        if (pedidoFromJson == null || pedidoFromJson.isEmpty()) {
            pedido.setId(clienteId);
            pedido.setDataHora(LocalDateTime.now());

            try {
                String pedidoToJson = objectMapper.writeValueAsString(pedido);
                redisTemplate.opsForValue().set(String.format("PEDIDO%d", pedido.getId()), pedidoToJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Já existe um carrinho em aberto para este usuário.");
        }

        return pedido;
    }

    @Override
    public boolean delete(int id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Pedido pedidoFromDTO(PedidoDTO pedidoDto) {
        Cliente cliente = clienteRepository.findById(pedidoDto.getClienteId()).get();
        Pedido pedido = new Pedido(cliente);
        return pedido;
    }

    public Pedido savePedidoDTO(PedidoDTO pedidoDto) {
        Pedido pedido = pedidoFromDTO(pedidoDto);
        save(pedido);
        return pedido;
    }

    public Pedido finalizePedido(int id) {
        String pedidoRedisFromJson = (String) redisTemplate.opsForValue().get(String.format("PEDIDO%d", id));

        if (pedidoRedisFromJson == null || pedidoRedisFromJson.isEmpty()) {
            throw new IllegalArgumentException("Pedido inexistente.");
        }
        
        List<Item> itensFromPedidoRedis = null;
        Pedido pedidoRedis = null;

        try {
            pedidoRedis = objectMapper.readValue(pedidoRedisFromJson, Pedido.class);
            itensFromPedidoRedis = pedidoRedis.getItens();
            pedidoRedis.setId(null);
            pedidoRedis.setItens(new ArrayList<Item>());
            pedidoRedis.setFinalizado(true);
            pedidoRedis = pedidoRepository.save(pedidoRedis);
            redisTemplate.delete(String.format("PEDIDO%d", id));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } finally {
            for (Item item : itensFromPedidoRedis) {
                pedidoRedis.getItens().add(item);
                item.setId(null);
                item.setPedido(pedidoRedis);
                itemRepository.save(item);
            }
        }
        
        return pedidoRedis;
    }
    
}
