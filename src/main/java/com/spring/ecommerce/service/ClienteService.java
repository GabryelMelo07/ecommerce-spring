package com.spring.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.DTO.ClienteDTO;
import com.spring.ecommerce.model.Cliente;
import com.spring.ecommerce.repository.ClienteRepository;

@Service
public class ClienteService extends ClienteBase {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente getById(int id) {
        return clienteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente inexistente."));
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente update(int id, Cliente cliente) {
        Cliente clienteAtualizado = clienteRepository.findById(id).get();
        clienteAtualizado.setNome(cliente.getNome());
        clienteAtualizado.setEmail(cliente.getEmail());
        clienteAtualizado.setSenha(cliente.getSenha());
        return clienteRepository.save(clienteAtualizado);
    }

    @Override
    public boolean delete(int id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Cliente clienteFromDTO(ClienteDTO clienteDto) {
        Cliente cliente = new Cliente(clienteDto);
        return cliente;
    }
    
    public Cliente saveClienteDTO(ClienteDTO clienteDto) {
        Cliente cliente = clienteFromDTO(clienteDto);
        save(cliente);
        return cliente;
    }

    public Cliente updateClienteDTO(int id, ClienteDTO clienteDto) {
        Cliente clienteAtualizado = clienteFromDTO(clienteDto);
        clienteAtualizado.setId(id);
        update(id, clienteAtualizado);
        return clienteAtualizado;
    }

}
