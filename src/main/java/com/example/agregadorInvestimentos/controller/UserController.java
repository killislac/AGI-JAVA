package com.example.agregadorInvestimentos.controller;


import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLPermission;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){

       var userId =  userServices.creatUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users" + userId.toString())).build();
    }

    @GetMapping ("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){

        var user = userServices.getUserById(userId);
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<User>>listUsers(){
        var users = userServices.listUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{userid}")
    public ResponseEntity<User> uptadeUserById(@PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto){
    userServices.updateUserById(userId, updateUserDto);
        return  ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId")String userId){

        userServices.deleteUserbyId(userId);
        return  ResponseEntity.noContent().build();

    }
}
