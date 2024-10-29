package com.example.agregadorDeInvestimentos.repository;

import com.example.agregadorDeInvestimentos.entity.AccountStock;
import com.example.agregadorDeInvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
