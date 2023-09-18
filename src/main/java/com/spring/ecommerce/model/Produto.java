package com.spring.ecommerce.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.spring.ecommerce.DTO.ProdutoDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "produto")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String descricao;
    
    @Column
    private BigDecimal valor;
    
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private int estoque;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> itens;
    
    public Produto(ProdutoDTO produtoDto) {
        this.descricao = produtoDto.getDescricao();
        this.valor = produtoDto.getValor();
        this.estoque = produtoDto.getEstoque();
        this.itens = new ArrayList<>();
    }
    
}
