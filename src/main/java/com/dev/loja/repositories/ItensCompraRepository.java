package com.dev.loja.repositories;

import com.dev.loja.model.Compra;
import com.dev.loja.model.ItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensCompraRepository extends JpaRepository<ItensCompra, Long> {
}
