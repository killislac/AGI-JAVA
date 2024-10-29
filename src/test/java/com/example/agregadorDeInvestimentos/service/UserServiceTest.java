package com.example.agregadorDeInvestimentos.service;

import com.example.agregadorDeInvestimentos.dto.AccountCreateRecordDto;
import com.example.agregadorDeInvestimentos.dto.UserCreateRecordDto;
import com.example.agregadorDeInvestimentos.dto.UserUpdateRecordDto;
import com.example.agregadorDeInvestimentos.entity.Account;
import com.example.agregadorDeInvestimentos.entity.User;
import com.example.agregadorDeInvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private User userClass;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @InjectMocks
    private UserService userService;

    @Nested
    class create{

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUser()
        {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(user).when(repository).save(userArgumentCaptor.capture());

            var input = new UserCreateRecordDto(
                    "Usuario",
                    "usuario@gmail.com",
                    "123"
            );


            //Act
            var output = userService.create(input);

            //Assert
            var userCaptured = userArgumentCaptor.getValue();

            assertNotNull(output);
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }
        @Test
        @DisplayName("Should throws a exception when a error occurs")
        void shouldThrowExceptionWhenErrorOccurs()
        {
            doThrow(new RuntimeException()).when(repository).save(any());
            var input = new UserCreateRecordDto(
                    "Usuario",
                    "usuario@gmail.com",
                    "123"
            );

            assertThrows(RuntimeException.class, () -> userService.create(input));
        }
    }
    @Nested
    class find{
        @Test
        void shouldFindAUserByIdWithSuccessWhenOptionalIsPresent() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(repository).findById(uuidArgumentCaptor.capture());

            //act
            var output = userService.find(user.getId().toString());

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();

            assertTrue(output.isPresent());
            assertEquals(user.getId(), uuidCapture);
        }

        @Test
        void shouldFindAUserByIdWithSuccessWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());

            //act
            var output = userService.find(userId.toString());

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();

            assertTrue(output.isEmpty());
            assertEquals(userId, uuidCapture);
        }
    }
    @Nested
    class get{
        @Test
        void shouldReturnAllUsersWithSuccessWhenUsersHaveAccounts() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );

            var account = new Account(
                    UUID.randomUUID(),
                    "description",
                    user,
                    null,
                    null
            );


            var accountsList = List.of(account);
            user.setAccounts(accountsList);
            var usersList = List.of(user);

            doReturn(usersList)
                    .when(repository)
                    .findAll();



            var output = userService.get();

            assertNotNull(output);
            assertEquals(usersList.size(), output.size());
        }
        @Test
        void shouldReturnAllUsersWithSuccessWhenAUserNoHaveAccounts() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );

            var user2 = new User(
                    UUID.randomUUID(),
                    "Usuario2",
                    "usuario2@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );
            List<Account> accountList2 = new ArrayList<Account>();
            var account = new Account(
                    UUID.randomUUID(),
                    "description",
                    user,
                    null,
                    null
            );


            var accountsList = List.of(account);
            user.setAccounts(accountsList);
            user2.setAccounts(accountList2);
            var usersList = List.of(user, user2);

            doReturn(usersList)
                    .when(repository)
                    .findAll();



            var output = userService.get();

            assertNotNull(output);
            assertEquals(usersList.size(), output.size());
        }
    }
    @Nested
    class delete{
        @Test
        void shouldDeleteUserWithSuccessWhenUserExists() {

            doReturn(true)
                    .when(repository)
                    .existsById(uuidArgumentCaptor.capture());
            doNothing()
                    .when(repository)
                    .deleteById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            //act
            userService.delete(userId.toString());

            //assert
            var idList = uuidArgumentCaptor.getAllValues();


            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(repository, times(1)).existsById(idList.get(0));
            verify(repository, times(1)).deleteById(idList.get(1));
        }

        @Test
        void shouldNotDeleteUserWhenUsersNotExists() {

            doReturn(false)
                    .when(repository)
                    .existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //act
            userService.delete(userId.toString());

            //assert
            var uuid = uuidArgumentCaptor.getValue();


            assertEquals(userId, uuid);


            verify(repository, times(1)).existsById(uuid);
            verify(repository, times(0)).deleteById(any());

        }
    }
    @Nested
    class update{
        @Test
        @DisplayName("Should update a user, when this user exists and username and password is filled")
        void shouldUpdateUserWhenUserExistsAndUsernameAndPasswordIsFilled() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(repository)
                    .save(userArgumentCaptor.capture());
            var input = new UserUpdateRecordDto(
                    "newUsuario",
                    "newPassword"
            );
            //act
            userService.update(user.getId().toString(), input);

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();
            var userCapture = userArgumentCaptor.getValue();
            assertEquals(input.username(), userCapture.getUsername());
            assertEquals(input.password(), userCapture.getPassword());
            assertEquals(user.getId(), uuidCapture);

            verify(repository, times(1)).findById(uuidCapture);
            verify(repository, times(1)).save(userCapture);
        }

        @Test
        @DisplayName("Should update a user, when this user exists and username and password is filled")
        void shouldNotUpdateUserWhenUserNotExists() {

            doReturn(Optional.empty())
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());

            var uuid = UUID.randomUUID();
            var input = new UserUpdateRecordDto(
                    "newUsuario",
                    "newPassword"
            );
            //act
            userService.update(uuid.toString(), input);

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();

            assertEquals(uuid, uuidCapture);



            verify(repository, times(1)).findById(uuidCapture);
            verify(repository, times(0)).save(any());
        }
        @Test
        @DisplayName("Should update a user, when this user exists and username is filled")
        void shouldUpdateUserWhenUserExistsAndUsernameIsFilled() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user))
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(repository)
                    .save(userArgumentCaptor.capture());


            var input = new UserUpdateRecordDto(
                    "newUsuario",
                    null
            );
            //act
            userService.update(user.getId().toString(), input);

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();
            var userCapture = userArgumentCaptor.getValue();

            assertEquals(user.getId(), uuidCapture);
            assertNotEquals(input.password(), userCapture.getPassword());
            assertEquals(input.username(), userCapture.getUsername());



            verify(repository, times(1)).findById(uuidCapture);
            verify(repository, times(1)).save(userCapture);
        }
        @Test
        @DisplayName("Should update a user, when this user exists and password is filled")
        void shouldUpdateUserWhenUserExistsAndPasswordIsFilled() {
            var user = new User(
                    UUID.randomUUID(),
                    "Usuario",
                    "usuario@gmail.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user))
                    .when(repository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(repository)
                    .save(userArgumentCaptor.capture());


            var input = new UserUpdateRecordDto(
                    null,
                    "newPassword"
            );
            //act
            userService.update(user.getId().toString(), input);

            //assert
            var uuidCapture = uuidArgumentCaptor.getValue();
            var userCapture = userArgumentCaptor.getValue();

            assertEquals(user.getId(), uuidCapture);
            assertNotEquals(input.username(), userCapture.getUsername());
            assertEquals(input.password(), userCapture.getPassword());



            verify(repository, times(1)).findById(uuidCapture);
            verify(repository, times(1)).save(userCapture);
        }
    }
    @Nested
    class createAccount{
        @Test
        void shouldNotProceedWithAccountCreateIfNotUserExists() {
            //arrange
            UUID id = UUID.randomUUID();

                   doThrow(
                                    new ResponseStatusException(HttpStatus.NOT_FOUND)
                            )
                            .when(repository)
                            .findById(uuidArgumentCaptor.capture());




            AccountCreateRecordDto input = new AccountCreateRecordDto(
                    "description",
                    "street",
                    321
            );

            //act


            //assert
            assertThrows( ResponseStatusException.class, () -> userService.createAccount(id.toString(), input));
            assertEquals(id, uuidArgumentCaptor.getValue());
        }
    }

}