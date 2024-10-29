package com.example.agregadorDeInvestimentos.service;

import carval.adi.agregadorDeInvestimentos.dto.*;
import com.example.agregadorDeInvestimentos.dto.*;
import com.example.agregadorDeInvestimentos.entity.Account;
import com.example.agregadorDeInvestimentos.entity.BillingAdress;
import com.example.agregadorDeInvestimentos.entity.User;
import com.example.agregadorDeInvestimentos.repository.AccountRepository;
import com.example.agregadorDeInvestimentos.repository.BillingAdressRepository;
import com.example.agregadorDeInvestimentos.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository repository;
    private AccountRepository accountRepository;
    private BillingAdressRepository billingAdressRepository;

    public UserService(UserRepository repository, AccountRepository accountRepository, BillingAdressRepository billingAdressRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.billingAdressRepository = billingAdressRepository;

    }

    public UUID create(UserCreateRecordDto userCreateRecordDto)
    {
        User user = new User(
                UUID.randomUUID(),
                userCreateRecordDto.username(),
                userCreateRecordDto.email(),
                userCreateRecordDto.password(),
                Instant.now(),
                null
        );


        var saved = repository.save(user);

        return saved.getId();
    }

    public Optional<User> find(String id)
    {
        Optional<User> optionalUser = repository.findById(UUID.fromString(id));
        return optionalUser;
    }

    public List<UserResponseRecordDto> get()
    {

        var users = repository.findAll()
                .stream()
                .map(us-> new UserResponseRecordDto(
                        us.getId().toString(),
                        us.getUsername(),
                        us.getEmail(),
                        us.getAccounts().size(),
                        us.getCreated_at().toString()
                ))
                .toList();
        return  users;
    }

    public void delete(String id)
    {
        boolean userExist = repository.existsById(UUID.fromString(id));
        if(userExist) {
            repository.deleteById(UUID.fromString(id));
        }

    }
    public void update(String id, UserUpdateRecordDto userUpdateRecordDto)
    {
        var userEntity = repository.findById(UUID.fromString(id));
        if(userEntity.isPresent()){
            var user = userEntity.get();
            if(userUpdateRecordDto.username() != null){
                user.setUsername(userUpdateRecordDto.username());
            }
            if(userUpdateRecordDto.password() != null){
                user.setPassword(userUpdateRecordDto.password());
            }
            repository.save(user);
        }
    }


    public void createAccount(String id, AccountCreateRecordDto accountCreateRecordDto)
    {
        var user = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        Account account = new Account(
                UUID.randomUUID(),
                accountCreateRecordDto.description(),
                user,
                null,
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        BillingAdress billingAdress = new BillingAdress(
                accountCreated.getId(),
                accountCreateRecordDto.street(),
                accountCreateRecordDto.number(),
                accountCreated
        );

        billingAdressRepository.save(billingAdress);

    }

    public List<AccountResponseDto> getAccounts(String id)
    {
        var user = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var accounts = user.getAccounts()
                .stream()
                .map(ac
                        -> new AccountResponseDto(
                                ac.getId().toString(), ac.getDescription()
                ))
                .toList();
        return accounts;
    }
}
