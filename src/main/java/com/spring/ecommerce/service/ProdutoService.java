package com.spring.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.DTO.ProdutoDTO;
import com.spring.ecommerce.model.Produto;
import com.spring.ecommerce.repository.ProdutoRepository;

@Service
public class ProdutoService extends ProdutoBase {

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
        return produtoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto inexistente."));
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
        produtoAtualizado.setValor(produto.getValor());
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
    
    private Produto produtoFromDTO(ProdutoDTO produtoDto) {
        Produto produto = new Produto(produtoDto);
        return produto;
    }

    public Produto saveProdutoDTO(ProdutoDTO produtoDto) {
        Produto produto = produtoFromDTO(produtoDto);
        save(produto);
        return produto;
    }

    public Produto updateProdutoDTO(int id, ProdutoDTO produtoDto) {
        Produto produtoAtualizado = produtoFromDTO(produtoDto);
        produtoAtualizado.setId(id);
        update(id, produtoAtualizado);
        return produtoAtualizado;
    }
    
}
