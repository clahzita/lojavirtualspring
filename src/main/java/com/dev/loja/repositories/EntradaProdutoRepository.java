package com.dev.loja.repositories;

import com.dev.loja.model.EntradaProduto;
import com.dev.loja.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaProdutoRepository extends JpaRepository<EntradaProduto,Long> {
}
