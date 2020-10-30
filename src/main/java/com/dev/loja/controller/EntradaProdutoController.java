package com.dev.loja.controller;

import com.dev.loja.model.Cidade;
import com.dev.loja.model.EntradaItem;
import com.dev.loja.model.EntradaProduto;
import com.dev.loja.model.Produto;
import com.dev.loja.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EntradaProdutoController {

    private List<EntradaItem> listaEntradaItem = new ArrayList<>();

    @Autowired
    private EntradaProdutoRepository entradaProdutoRepository;

    @Autowired
    private EntradaItemRepository entradaItemRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/administrativo/entrada/cadastrar")
    public ModelAndView cadastrar(EntradaProduto entrada, EntradaItem entradaItem){
        ModelAndView mv = new ModelAndView("administrativo/entrada/cadastro");
        mv.addObject("entrada", entrada);
        mv.addObject("entradaItens", entradaItem);
        mv.addObject("listaEntradaItens",this.listaEntradaItem);
        mv.addObject("listaFuncionarios", funcionarioRepository.findAll());
        mv.addObject("listaProdutos", produtoRepository.findAll());
        return mv;
    }

//    @GetMapping("/administrativo/cidades/listar")
//    public ModelAndView listar(){
//        ModelAndView mv = new ModelAndView("administrativo/cidades/lista");
//        mv.addObject("listaCidades", cidadeRepository.findAll());
//        return mv;
//    }
//
//    @GetMapping("/administrativo/cidades/editar/{id}")
//    public ModelAndView editar(@PathVariable("id") Long id){
//        Optional<Cidade> cidade = cidadeRepository.findById(id);
//        return cadastrar(cidade.get());
//    }
//
//    @GetMapping("/administrativo/cidades/remover/{id}")
//    public ModelAndView remover(@PathVariable("id") Long id){
//        Optional<Cidade> cidade = cidadeRepository.findById(id);
//        cidadeRepository.delete(cidade.get());
//        return listar();
//    }

    @PostMapping("/administrativo/entrada/salvar")
    public ModelAndView salvar(String acao, @Valid EntradaProduto entrada, EntradaItem entradaItem){
        if(acao.equals("itens")) {
            this.listaEntradaItem.add(entradaItem);
        }else if (acao.equals("salvar")){
            entradaProdutoRepository.saveAndFlush(entrada);
            listaEntradaItem.forEach(item -> {
                item.setEntrada(entrada);
                entradaItemRepository.saveAndFlush(item);
                atualizarProduto(item);
                this.listaEntradaItem = new ArrayList<>();
            });

            return cadastrar(new EntradaProduto(), new EntradaItem());
        }
        //retorna a tela de formulario em branco
        return cadastrar(entrada, new EntradaItem());
    }

    private void atualizarProduto(EntradaItem item) {
        Optional<Produto> prod = produtoRepository.findById(item.getProduto().getId());
        Produto produto = prod.get();
        Double novaQuantidade = produto.getQuantidadeEstoque() + item.getQuantidade();
        produto.setQuantidadeEstoque(novaQuantidade);
        produto.setValorVenda(item.getValorVenda());
        produtoRepository.saveAndFlush(produto);

    }
}
