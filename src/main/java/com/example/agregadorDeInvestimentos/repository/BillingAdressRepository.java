package com.example.agregadorDeInvestimentos.repository;

import com.example.agregadorDeInvestimentos.entity.BillingAdress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingAdressRepository extends JpaRepository<BillingAdress, UUID> {
}
