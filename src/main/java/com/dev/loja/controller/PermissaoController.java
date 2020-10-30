package com.dev.loja.controller;

import com.dev.loja.model.Permissao;
import com.dev.loja.repositories.FuncionarioRepository;
import com.dev.loja.repositories.PapelRepository;
import com.dev.loja.repositories.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PermissaoController {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @GetMapping("/administrativo/permissoes/cadastrar")
    public ModelAndView cadastrar(Permissao permissao) {
        ModelAndView mv = new ModelAndView("administrativo/permissoes/cadastro");
        mv.addObject("permissao", permissao);
        mv.addObject("listaPapeis", papelRepository.findAll());
        mv.addObject("listaFuncionarios", funcionarioRepository.findAll());
        return mv;
    }

    @GetMapping("/administrativo/permissoes/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/permissoes/lista");
        mv.addObject("listaPermissoes", permissaoRepository.findAll());
        return mv;
    }

    @GetMapping("/administrativo/permissoes/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Permissao> permissao = permissaoRepository.findById(id);
        return cadastrar(permissao.get());
    }

    @GetMapping("/administrativo/permissoes/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Permissao> permissao = permissaoRepository.findById(id);
        permissaoRepository.delete(permissao.get());
        return listar();
    }

    @PostMapping("/administrativo/permissoes/salvar")
    public ModelAndView salvar(@Valid Permissao permissao, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(permissao);
        }
        permissaoRepository.saveAndFlush(permissao);

        //retorna a tela de formulario em branco
        return cadastrar(new Permissao());
    }
}
