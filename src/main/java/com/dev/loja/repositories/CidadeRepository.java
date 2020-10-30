package com.dev.loja.repositories;

import com.dev.loja.model.Cidade;
import com.dev.loja.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade,Long> {
}
