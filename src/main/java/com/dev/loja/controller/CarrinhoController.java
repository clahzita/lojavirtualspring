package com.dev.loja.controller;

import com.dev.loja.model.Compra;
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
    private final Compra compra = new Compra();
    @Autowired
    private ProdutoRepository produtoRepository;

    //TODO colocar operações em camada de serviço
    private void calcularTotal() {
        itensCompra.values().forEach(i -> compra.setValorTotal(compra.getValorTotal() + i.getValorTotal()));

    }

    @GetMapping("/carrinho")
    public ModelAndView exibirCarrinho() {
        ModelAndView mv = new ModelAndView("/cliente/carrinho");
        mv.addObject("compra", compra);
        mv.addObject("listaItens", itensCompra.values());
        return mv;
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String adicionarCarrinho(@PathVariable Long id) {
        Optional<Produto> prod = produtoRepository.findById(id);
        Produto produto = prod.get();

        //TODO Verificar quantidade no estoque
        ItensCompra item;
        if (itensCompra.containsKey(produto.getId())) {
            item = itensCompra.get(produto.getId());
            incrementaQuantidade(item);
            itensCompra.replace(produto.getId(), item);
        } else {
            item = new ItensCompra();
            item.setCompra(compra);
            item.setProduto(produto);
            item.setValorUnitario(produto.getValorVenda());
            incrementaQuantidade(item);
            itensCompra.put(produto.getId(), item);
        }

        ArrayList<ItensCompra> listaItens = new ArrayList<ItensCompra>(itensCompra.values());

        return "redirect:/carrinho";
    }

    private void incrementaQuantidade(ItensCompra item) {
        item.setQuantidade(item.getQuantidade() + 1);
        compra.setValorTotal(compra.getValorTotal() + item.getValorUnitario());
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
    }

    private void decrementaQuantidade(ItensCompra item) {
        if (item.getQuantidade() == 1) {
            removerProdutoCarrinho(item.getProduto().getId());
        } else if (item.getQuantidade() > 1) {
            item.setQuantidade(item.getQuantidade() - 1);
            compra.setValorTotal(compra.getValorTotal() - item.getValorUnitario());
            item.setValorTotal(item.getQuantidade() * item.getValorUnitario());
        }
    }

    @GetMapping("/alterarQuantidade/{id}/{acao}")
    public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
        ItensCompra item = itensCompra.get(id);

        if (item != null) {
            if (acao == 0) {
                decrementaQuantidade(item);
            } else if (acao == 1) {
                incrementaQuantidade(item);
            }
            itensCompra.replace(id, item);
        }

        return "redirect:/carrinho";

    }

    @GetMapping("/removerProduto/{id}")
    public String removerProdutoCarrinho(@PathVariable Long id) {

        ItensCompra itemRemovido = itensCompra.remove(id);

        compra.setValorTotal(compra.getValorTotal() - itemRemovido.getValorTotal());

        return "redirect:/carrinho";
    }

    @GetMapping("/finalizar")
    public ModelAndView finalizarCompra() throws Exception {
        if(itensCompra.isEmpty()){
            throw new Exception();
        }
        ModelAndView mv = new ModelAndView("/cliente/finalizar");
        mv.addObject("compra", compra);
        mv.addObject("listaItens", itensCompra.values());
        return mv;
    }
}
