package com.dev.loja.repositories;

import com.dev.loja.model.Estado;
import com.dev.loja.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
}
