package com.spring.ecommerce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ecommerce.DTO.ItemDTO;
import com.spring.ecommerce.model.Item;
import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.model.Produto;
import com.spring.ecommerce.repository.ItemRepository;
import com.spring.ecommerce.repository.ProdutoRepository;

@Service
public class ItemService extends ItemBase {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ItemService(ItemRepository itemRepository, ProdutoRepository produtoRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.itemRepository = itemRepository;
        this.produtoRepository = produtoRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item getById(int id) {
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item inexistente."));
    }

    @Override
    public Item save(Item item) {
        Pedido pedidoRedis = getPedidoFromRedis(String.format("PEDIDO%d", item.getPedido().getId()));
        Produto produto = produtoRepository.findById(item.getProduto().getId()).get();
        
        if (pedidoRedis.getItens() == null) {
            pedidoRedis.setItens(new ArrayList<Item>());
        }

        if (produto.getEstoque() - item.getQuantidade() < 0) {
            throw new IllegalStateException(String.format("Produto não tem a quantidade suficiente em estoque. Restam: %d unidades.", produto.getEstoque()));
        }

        pedidoRedis.getItens().add(item);
        pedidoRedis.refreshValorAtual();
        produto.setEstoque(produto.getEstoque() - item.getQuantidade());
        produtoRepository.save(produto);
        savePedidoToRedis(pedidoRedis);
        return item;
    }

    @Override
    public Item update(int id, Item item) {
        Pedido pedidoRedis = getPedidoFromRedis(String.format("PEDIDO%d", item.getPedido().getId()));

        for (Item itemPedido : pedidoRedis.getItens()) {
            if(itemPedido.getId() == item.getId()) {
                itemPedido.setId(id);
                itemPedido.setQuantidade(item.getQuantidade());
                itemPedido.setProduto(item.getProduto());
                itemPedido.setValor(item.getProduto().getValor().multiply(BigDecimal.valueOf(item.getQuantidade())));
                pedidoRedis.refreshValorAtual();
                savePedidoToRedis(pedidoRedis);
                return itemPedido;
            }
        }
        
        return null;
    }

    @Override
    public boolean delete(int id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item inexistente."));
        Produto produto = produtoRepository.findById(item.getProduto().getId()).get();
        produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        produtoRepository.save(produto);
        itemRepository.deleteById(id);
        return true;
    }

    private Pedido getPedidoFromRedis(String key) {
        String pedidoJson = (String) redisTemplate.opsForValue().get(key);

        if (pedidoJson == null || pedidoJson.isEmpty()) {
            throw new RuntimeException("Pedido inexistente.");
        }

        try {
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);
            return pedido;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao desserializar o objeto de Pedido do Redis.");
        }
    }

    private void savePedidoToRedis(Pedido pedido) {
        try {
            String pedidoRedisToJson = objectMapper.writeValueAsString(pedido);
            redisTemplate.opsForValue().set(String.format("PEDIDO%d", pedido.getId()), pedidoRedisToJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Item itemFromDTO(ItemDTO itemDto) {
        Pedido pedido = getPedidoFromRedis(String.format("PEDIDO%d", itemDto.getPedidoId()));
        Produto produto = produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(() -> new IllegalArgumentException("Produto inexistente."));
        Item item = new Item(itemDto.getQuantidade(), pedido, produto);
        item.setId(pedido.getItens().size() + 1);
        return item;
    }

    public Item saveItemDTO(ItemDTO itemDto) {
        Item item = itemFromDTO(itemDto);
        if (!item.getPedido().isFinalizado()) {
            save(item);
        } else {
            throw new IllegalArgumentException("Pedido já está finalizado, impossível atualizá-lo.");
        }
        return item;
    }

    public Item updateItemDTO(int id, ItemDTO itemDto) {
        Item item = itemFromDTO(itemDto);
        item.setId(id);
        update(id, item);
        return item;
    }

}
