package com.example.agregadorDeInvestimentos.service;

import com.example.agregadorDeInvestimentos.dto.AccountStockCreateRecordDto;
import com.example.agregadorDeInvestimentos.dto.AccountStockGetRecordDto;
import com.example.agregadorDeInvestimentos.entity.AccountStock;
import com.example.agregadorDeInvestimentos.entity.AccountStockId;
import com.example.agregadorDeInvestimentos.repository.AccountRepository;
import com.example.agregadorDeInvestimentos.repository.AccountStockRepository;
import com.example.agregadorDeInvestimentos.repository.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
    }

    public void createAccountStock(String accountId, AccountStockCreateRecordDto accountStockCreateRecordDto)
    {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var stock = stockRepository.findById(accountStockCreateRecordDto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        AccountStockId accountStockId = new AccountStockId(
                account.getId(),
                stock.getId()
        );
        AccountStock accountStock = new AccountStock(
            accountStockId,
            account,
            stock,
            accountStockCreateRecordDto.quantity()
        );

        accountStockRepository.save(accountStock);
    }

    public List<AccountStockGetRecordDto> getAccountStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var accountStocksaccount = account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockGetRecordDto(
                        as.getStock().getId(),
                        as.getStock().getDescription(),
                        as.getQuantity()
                ))
                .toList();

        return accountStocksaccount;


    }
}
