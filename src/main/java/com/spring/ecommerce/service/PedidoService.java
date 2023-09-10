package com.spring.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.repository.PedidoRepository;
import com.spring.ecommerce.service.interfaces.ICrudService;

@Service
public class PedidoService implements ICrudService<Pedido> {

    @Autowired
    private PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
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
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido update(int id, Pedido pedido) {
        Pedido pedidoAtualizado = pedidoRepository.findById(id).get();
        pedidoAtualizado.setDataHora(LocalDateTime.now());
        pedidoAtualizado.setCliente(pedido.getCliente());
        pedidoAtualizado.setItens(pedido.getItens());
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
