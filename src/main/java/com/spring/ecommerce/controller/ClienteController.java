package com.spring.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ecommerce.DTO.ClienteDTO;
import com.spring.ecommerce.model.Cliente;
import com.spring.ecommerce.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        List<Cliente> clientes = clienteService.getAll();
        return ResponseEntity.ok().body(clientes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable int id) {
        Cliente cliente = clienteService.getById(id);
        return ResponseEntity.ok().body(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody ClienteDTO clienteDto) {
        Cliente cliente = clienteService.saveClienteDTO(clienteDto);
        return ResponseEntity.ok().body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable int id, @RequestBody ClienteDTO clienteDto) {
        Cliente cliente = clienteService.updateClienteDTO(id, clienteDto);
        return ResponseEntity.ok().body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean deletado = clienteService.delete(id);
        if (deletado) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}
