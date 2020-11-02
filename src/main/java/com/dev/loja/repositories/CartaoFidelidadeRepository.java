package com.dev.loja.repositories;

import com.dev.loja.model.CartaoFidelidade;
import com.dev.loja.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoFidelidadeRepository extends JpaRepository<CartaoFidelidade,Long> {
}
