package com.spring.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Produto;
import com.spring.ecommerce.repository.ProdutoRepository;
import com.spring.ecommerce.service.interfaces.ICrudService;

@Service
public class ProdutoService implements ICrudService<Produto> {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public List<Produto> getAll() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto getById(int id) {
        return produtoRepository.findById(id).get();
    }

    @Override
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Produto update(int id, Produto produto) {
        Produto produtoAtualizado = produtoRepository.findById(id).get();
        produtoAtualizado.setDescricao(produto.getDescricao());
        produtoAtualizado.setEstoque(produto.getEstoque());
        produtoAtualizado.setValor(produtoAtualizado.getValor());
        return produtoRepository.save(produtoAtualizado);
    }

    @Override
    public boolean delete(int id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}
