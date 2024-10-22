package com.example.agregadorInvestimentos.services;

import com.example.agregadorInvestimentos.controller.CreateUserDto;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServices userServices;

    @Nested
    class createUser{
        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUser(){
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "passaword",
                    Instant.now(),
                    null
            );

            //Arrange
            doReturn(null).when(userRepository).save(any());
            var input = new CreateUserDto("", "", "");
            //Act
            var output = userServices.creatUser(input);
            //Assert
            assertNotNull(output);

        }
        @Test
        @DisplayName("Should throw exception when erros occurs")
        void shouldThrowExceptionWhenErrosOccurs(){
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "passaword",
                    Instant.now(),
                    null
            );

            //Arrange
            doThrow( new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto("", "", "");
            //Act
            assertThrows(RuntimeException.class, () ->  userServices.creatUser(input));


        }
    }
}