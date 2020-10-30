package com.dev.loja.controller;

import com.dev.loja.model.Estado;
import com.dev.loja.model.Produto;
import com.dev.loja.repositories.EstadoRepository;
import com.dev.loja.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Controller
public class ProdutoController {

    private static String caminhoImagens = "E:\\Projetos\\Java\\imagens-loja\\";
    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/administrativo/produtos/cadastrar")
    public ModelAndView cadastrar(Produto produto){
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        return mv;
    }

    @GetMapping("/administrativo/produtos/listar")
    public ModelAndView listar(){
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProdutos", produtoRepository.findAll());
        return mv;
    }

    @GetMapping("/administrativo/produtos/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id){
        Optional<Produto> produto = produtoRepository.findById(id);
        return cadastrar(produto.get());
    }

    @GetMapping("/administrativo/produtos/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id){
        Optional<Produto> produto = produtoRepository.findById(id);
        produtoRepository.delete(produto.get());
        return listar();
    }

    @GetMapping("/administrativo/produtos/mostrarImagem/{imagem}")
    @ResponseBody
    public byte[] retornarImagem(@PathVariable("imagem") String imagem){
        File imagemArquivo = new File(caminhoImagens+imagem);
        if(imagem != null || imagem.trim().length() > 0){
            try {
                return Files.readAllBytes(imagemArquivo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @PostMapping("/administrativo/produtos/salvar")
    public ModelAndView salvar(@Valid Produto produto, BindingResult result, @RequestParam("file")MultipartFile arquivo){
        if(result.hasErrors()){
            return cadastrar(produto);
        }

        produtoRepository.saveAndFlush(produto);

        if(!arquivo.isEmpty()){
            salvarArquivoImagem(produto,arquivo);
        }

        //retorna a tela de formulario em branco
        return cadastrar(new Produto());
    }

    private void salvarArquivoImagem(Produto novoProduto, MultipartFile arquivo) {
        try {
            byte[] bytes =  arquivo.getBytes();
            String nomeImagem = String.valueOf(novoProduto.getId()+arquivo.getOriginalFilename());
            Path caminho = Paths.get(caminhoImagens+nomeImagem);
            Files.write(caminho,bytes);
            novoProduto.setNomeImagem(nomeImagem);
            produtoRepository.saveAndFlush(novoProduto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
