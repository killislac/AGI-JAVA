package com.example.agregadorDeInvestimentos.controller;

import com.example.agregadorDeInvestimentos.dto.StockCreateRecordDto;
import com.example.agregadorDeInvestimentos.entity.Stock;
import com.example.agregadorDeInvestimentos.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {
    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Stock> create(@RequestBody StockCreateRecordDto body)
    {
        var id = service.create(body);
        return ResponseEntity.created(URI.create("/v1/stocks/" + id)).build();
    }
   }