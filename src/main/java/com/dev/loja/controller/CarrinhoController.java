package com.dev.loja.controller;

import com.dev.loja.model.Cliente;
import com.dev.loja.model.Compra;
import com.dev.loja.model.ItensCompra;
import com.dev.loja.repositories.ClienteRepository;
import com.dev.loja.repositories.CompraRepository;
import com.dev.loja.repositories.ItensCompraRepository;
import com.dev.loja.repositories.ProdutoRepository;
import com.dev.loja.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CarrinhoController {

    Map<Long, ItensCompra> itensCompra = new HashMap<>();
    private Compra compra = new Compra();
    private Cliente cliente;

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ItensCompraRepository itensCompraRepository;

    public CarrinhoController() {
    }

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

        carrinhoService.adcionarProdutoCarrinho(id, itensCompra, compra);

        return "redirect:/carrinho";
    }

    @GetMapping("/alterarQuantidade/{id}/{acao}")
    public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
        ItensCompra item = itensCompra.get(id);

        if (item != null) {
            if (acao == 0) {
                if (item.getQuantidade() == 1) {
                    removerProdutoCarrinho(item.getProduto().getId());
                } else if (item.getQuantidade() > 1) {
                    carrinhoService.decrementaQuantidade(item, compra);
                }
            } else if (acao == 1) {
                carrinhoService.incrementaQuantidade(item, compra);
            }
            itensCompra.replace(id, item);
        }

        return "redirect:/carrinho";

    }

    @GetMapping("/removerProduto/{id}")
    public String removerProdutoCarrinho(@PathVariable Long id) {

        carrinhoService.removerProdutoCarrinho(id, itensCompra, compra);

        return "redirect:/carrinho";
    }

    private void buscarUsuarioAutenticao() {
        Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
        if (!(autenticado instanceof AnonymousAuthenticationToken)) {
            String email = autenticado.getName();
            //TODO Garantir que so tem um cliente com determinado e-mail
            cliente = clienteRepository.findByEmail(email).get(0);
        }

    }

    @GetMapping("/finalizar")
    public ModelAndView finalizarCompra() throws Exception {
        buscarUsuarioAutenticao();
        if (itensCompra.isEmpty()) {
            throw new Exception();
        }
        ModelAndView mv = new ModelAndView("/cliente/finalizar");
        mv.addObject("compra", compra);
        mv.addObject("listaItens", itensCompra.values());
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PostMapping("/finalizar/confirmar")
    public ModelAndView confirmarCompra(String formaPagamento) {
        ModelAndView mv = new ModelAndView("cliente/compraFinalizada");
        compra.setCliente(cliente);
        compra.setFormaPagamento(formaPagamento);
        compraRepository.saveAndFlush(compra);
        itensCompraRepository.saveAll(itensCompra.values());
        mv.addObject("cliente", cliente);
        itensCompra = new HashMap<>();
        compra = new Compra();

        return mv;
    }


}
