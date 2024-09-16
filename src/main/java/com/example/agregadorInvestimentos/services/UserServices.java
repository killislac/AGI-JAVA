package com.example.agregadorInvestimentos.services;

import com.example.agregadorInvestimentos.controller.CreateUserDto;
import com.example.agregadorInvestimentos.controller.UpdateUserDto;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {

    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID creatUser(CreateUserDto createUserDto){

        var entity = new User(UUID.randomUUID(), createUserDto.username(), createUserDto.email(), createUserDto.password(), Instant.now(), null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserid();
    }

    public Optional<User> getUserById(String userId){
       return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }


    public void updateUserById(String userId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);
        var userExists = userRepository.findById(id);
        if (userExists.isPresent()) {
            var user = userExists.get();
            if (updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }

            if (updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }

            userRepository.save(user);
        }

    }
    public void deleteUserbyId(String userId){
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if(userExists){
            userRepository.deleteById(id);
        }
    }
}
