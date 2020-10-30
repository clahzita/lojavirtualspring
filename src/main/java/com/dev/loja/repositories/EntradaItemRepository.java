package com.dev.loja.repositories;

import com.dev.loja.model.EntradaItem;
import com.dev.loja.model.EntradaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaItemRepository extends JpaRepository<EntradaItem,Long> {
}
