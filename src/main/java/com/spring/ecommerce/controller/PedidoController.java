package com.spring.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ecommerce.DTO.PedidoDTO;
import com.spring.ecommerce.model.Pedido;
import com.spring.ecommerce.service.PedidoService;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    
    @GetMapping
    public ResponseEntity<List<Pedido>> getAll() {
        List<Pedido> pedidos = pedidoService.getAll();
        return ResponseEntity.ok().body(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable int id) {
        Pedido pedido = pedidoService.getById(id);
        return ResponseEntity.ok().body(pedido);
    }

    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody PedidoDTO pedidoDto) {
        Pedido pedido = pedidoService.savePedidoDTO(pedidoDto);
        return ResponseEntity.ok().body(pedido);
    }

    @GetMapping("/finalizar/{id}")
    public ResponseEntity<Pedido> finalizePedido(@PathVariable int id) {
        Pedido pedido = pedidoService.finalizePedido(id);
        return ResponseEntity.ok().body(pedido);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<Pedido> update(@PathVariable int id, @RequestBody Pedido pedido) {
    //     pedidoService.update(id, pedido);
    //     return ResponseEntity.ok().body(pedido);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean deletado = pedidoService.delete(id);
        if (deletado) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
    
}
