package com.example.agregadorDeInvestimentos.service;

import com.example.agregadorDeInvestimentos.dto.StockCreateRecordDto;
import com.example.agregadorDeInvestimentos.entity.Stock;
import com.example.agregadorDeInvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public String create(StockCreateRecordDto body) {
        Stock stock = new Stock(
                body.id(),
                body.description()
        );
        var stockCreated = stockRepository.save(stock);

        return stockCreated.getId();
    }
}
