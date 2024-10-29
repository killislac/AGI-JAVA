package com.example.agregadorDeInvestimentos.controller;

import carval.adi.agregadorDeInvestimentos.dto.*;
import com.example.agregadorDeInvestimentos.dto.*;
import com.example.agregadorDeInvestimentos.entity.User;
import com.example.agregadorDeInvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserCreateRecordDto body)
    {
        var id = service.create(body);
        return ResponseEntity.created(URI.create("/v1/users/" + id.toString())).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(name = "id") String id)
    {
        var user = service.find(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseRecordDto>> get()
    {
        List<UserResponseRecordDto> users = service.get();
        if(!users.isEmpty()){
            return  ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> destroy(@PathVariable(name = "id") String id)
    {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> edit(@PathVariable(name = "id") String id, @RequestBody UserUpdateRecordDto userUpdateRecordDto)
    {
        service.update(id, userUpdateRecordDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/accounts")
    public ResponseEntity<Void> storeAccount(@PathVariable(name = "id") String id,
                                             @RequestBody AccountCreateRecordDto accountCreateRecordDto)
    {
        service.createAccount(id, accountCreateRecordDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<AccountResponseDto>> getAccount(@PathVariable(name = "id") String id)
    {
        var accounts = service.getAccounts(id);
        return ResponseEntity.ok(accounts);
    }

}
