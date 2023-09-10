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

import com.spring.ecommerce.model.Produto;
import com.spring.ecommerce.service.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> produtos = produtoService.getAll();
        return ResponseEntity.ok().body(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable int id) {
        Produto produto = produtoService.getById(id);
        return ResponseEntity.ok().body(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody Produto produto) {
        produtoService.save(produto);
        return ResponseEntity.ok().body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable int id, @RequestBody Produto produto) {
        produtoService.update(id, produto);
        return ResponseEntity.ok().body(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean deletado = produtoService.delete(id);
        if (deletado) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
    
}
