package com.dev.loja.controller;

import com.dev.loja.model.ItensCompra;
import com.dev.loja.model.Produto;
import com.dev.loja.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CarrinhoController {

    Map<Long, ItensCompra> itensCompra = new HashMap<>();

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/carrinho")
    public ModelAndView exibirCarrinho() {
        ModelAndView mv = new ModelAndView("/cliente/carrinho");
        mv.addObject("listaItens", itensCompra.values());
        return mv;
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public ModelAndView adicionarCarrinho(@PathVariable Long id) {
        Optional<Produto> prod = produtoRepository.findById(id);
        Produto produto = prod.get();

        //TODO Verificar quantidade no estoque

        ItensCompra item = new ItensCompra();
        if (itensCompra.containsKey(produto.getId())) {
            item = itensCompra.get(produto.getId());
            incrementaQuantidade(item);
            itensCompra.replace(produto.getId(), item);
        } else {
            item.setProduto(produto);
            item.setValorUnitario(produto.getValorVenda());
            incrementaQuantidade(item);
            itensCompra.put(produto.getId(), item);
        }

        ArrayList<ItensCompra> listaItens = new ArrayList<ItensCompra>(itensCompra.values());
        ModelAndView mv = new ModelAndView("/cliente/carrinho");
        mv.addObject("listaItens", listaItens);

        return mv;
    }

    private void incrementaQuantidade(ItensCompra item) {
        item.setQuantidade(item.getQuantidade() + 1);
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
    }

    private void decrementaQuantidade(ItensCompra item) {
        item.setQuantidade(item.getQuantidade() - 1);
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
    }

    @GetMapping("/alterarQuantidade/{id}/{acao}")
    public ModelAndView alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
        ItensCompra item = itensCompra.get(id);

        if(item != null){
            if(acao == 0){
                decrementaQuantidade(item);
            }else if (acao==1){
                incrementaQuantidade(item);
            }
            itensCompra.replace(id,item);
        }

        ModelAndView mv = new ModelAndView("/cliente/carrinho");
        mv.addObject("listaItens", itensCompra.values());
        return mv;
    }

}
