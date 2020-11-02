package com.dev.loja.controller;

import com.dev.loja.model.CartaoFidelidade;
import com.dev.loja.model.Cliente;
import com.dev.loja.repositories.CartaoFidelidadeRepository;
import com.dev.loja.repositories.CidadeRepository;
import com.dev.loja.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CartaoFidelidadeRepository cartaoFidelidadeRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/cliente/cadastrar")
    public ModelAndView cadastrar(Cliente cliente) {
        ModelAndView mv = new ModelAndView("cliente/cadastrar");
        mv.addObject("cliente", cliente);
        mv.addObject("listaCidades", cidadeRepository.findAll());
        return mv;
    }

    @GetMapping("/cliente/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cadastrar(cliente.get());
    }

    @GetMapping("/cliente/remover/{id}")
    public String remover(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        cliente.ifPresent(c -> clienteRepository.delete(c));
        clienteRepository.delete(cliente.get());
        return "redirect:/";
    }

    @PostMapping("/cliente/salvar")
    public ModelAndView salvar(@Valid Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cliente);
        }
        //TODO verificar se está ou não editando a senha
        cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));

        if(cliente.getId() == null){
            CartaoFidelidade cartao = new CartaoFidelidade();
            cartao.setCliente(cliente);
            cliente.setCartaoFidelidade(cartao);
            cartao = cartaoFidelidadeRepository.save(cartao);
        }

        clienteRepository.saveAndFlush(cliente);

        //retorna a tela de formulario em branco
        return cadastrar(new Cliente());
    }
}
