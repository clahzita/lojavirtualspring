package com.dev.loja.controller;

import com.dev.loja.model.Produto;
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
import java.util.Optional;

@Controller
public class ImagemController {

    private static String caminhoImagens = "E:\\Projetos\\Java\\imagens-loja\\";
    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/mostrarImagem/{imagem}")
    @ResponseBody
    public byte[] exibirImagem(@PathVariable("imagem") String imagem){
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

}
