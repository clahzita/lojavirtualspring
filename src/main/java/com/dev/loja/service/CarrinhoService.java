package com.dev.loja.service;

import com.dev.loja.model.Compra;
import com.dev.loja.model.ItensCompra;
import com.dev.loja.model.Produto;
import com.dev.loja.repositories.ClienteRepository;
import com.dev.loja.repositories.CompraRepository;
import com.dev.loja.repositories.ItensCompraRepository;
import com.dev.loja.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ItensCompraRepository itensCompraRepository;

    public void adcionarProdutoCarrinho(Long produtoId, Map<Long, ItensCompra> itensCompra, Compra compra) {
        Optional<Produto> prod = produtoRepository.findById(produtoId);
        Produto produto = prod.get();

        //TODO Verificar quantidade no estoque
        ItensCompra item;
        if (itensCompra.containsKey(produto.getId())) {
            item = itensCompra.get(produto.getId());
            incrementaQuantidade(item, compra);
            itensCompra.replace(produto.getId(), item);
        } else {
            item = new ItensCompra();
            item.setCompra(compra);
            item.setProduto(produto);
            item.setValorUnitario(produto.getValorVenda());
            incrementaQuantidade(item, compra);
            itensCompra.put(produto.getId(), item);
        }
    }

    public void incrementaQuantidade(ItensCompra item, Compra compra) {
        item.setQuantidade(item.getQuantidade() + 1);
        compra.setValorTotal(compra.getValorTotal() + item.getValorUnitario());
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
    }

    public void decrementaQuantidade(ItensCompra item, Compra compra) {
        item.setQuantidade(item.getQuantidade() - 1);
        compra.setValorTotal(compra.getValorTotal() - item.getValorUnitario());
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
    }

    public void removerProdutoCarrinho(Long produtoId, Map<Long, ItensCompra> itensCompra, Compra compra) {
        ItensCompra itemRemovido = itensCompra.remove(produtoId);
        compra.setValorTotal(compra.getValorTotal() - itemRemovido.getValorTotal());
    }
}
