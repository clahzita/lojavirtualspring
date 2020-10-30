package com.dev.loja.repositories;

import com.dev.loja.model.Cidade;
import com.dev.loja.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PapelRepository extends JpaRepository<Papel,Long> {
}
