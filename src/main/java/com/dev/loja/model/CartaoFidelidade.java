package com.dev.loja.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="cartao_fidelidade")
public class CartaoFidelidade implements Serializable {

    public CartaoFidelidade() {
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "cartaoFidelidade")
    private Cliente cliente;

    private Integer pontos=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }
}
