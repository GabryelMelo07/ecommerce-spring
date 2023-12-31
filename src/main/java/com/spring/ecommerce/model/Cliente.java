package com.spring.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import com.spring.ecommerce.DTO.ClienteDTO;

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
@Table(name = "cliente")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 60, unique = true, nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String senha;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pedido> pedidos;
    
    public Cliente(ClienteDTO clienteDto) {
        this.nome = clienteDto.getNome();
        this.email = clienteDto.getEmail();
        this.senha = clienteDto.getSenha();
        this.pedidos = new ArrayList<>();
    }
    
}
