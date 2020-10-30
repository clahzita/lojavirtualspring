package com.dev.loja.repositories;

import com.dev.loja.model.Cidade;
import com.dev.loja.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao,Long> {
}
