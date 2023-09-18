package com.spring.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column
    private LocalDateTime dataHora;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean finalizado;

    @Column
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties("pedidos")
    private Cliente cliente;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pedido")
    private List<Item> itens;
    
    public Pedido(Cliente cliente) {
        this.dataHora = LocalDateTime.now();
        this.finalizado = false;
        this.cliente = cliente;
        this.itens = new ArrayList<Item>();
    }
    
    public void refreshValorAtual() {
        this.valorTotal = BigDecimal.ZERO;
        if (this.itens != null && !this.itens.isEmpty()) {
            for (Item item : this.itens) {
                this.valorTotal = this.valorTotal.add(item.getValor());
            }
        }
    }
    
}
