package com.dev.loja.controller;

import com.dev.loja.model.Cidade;
import com.dev.loja.repositories.CidadeRepository;
import com.dev.loja.repositories.EstadoRepository;
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
public class LogController {


    @GetMapping("/negado")
    public ModelAndView negarAcesso(){
        ModelAndView mv = new ModelAndView("/negado");

        return mv;
    }

    @GetMapping("/login")
    public ModelAndView logar(){
        ModelAndView mv = new ModelAndView("/login");

        return mv;
    }
}
