package com.dev.loja.repositories;

import com.dev.loja.model.Estado;
import com.dev.loja.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado,Long> {
}
