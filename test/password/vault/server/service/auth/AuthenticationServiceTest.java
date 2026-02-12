package password.vault.server.service.auth;

import org.junit.jupiter.api.Test;
import password.vault.server.algorithm.hashing.Sha256Hashing;
import password.vault.server.command.CommandResult;
import password.vault.server.repository.UserRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    UserRepository userRepository = mock(UserRepository.class);
    AuthenticationService authService = new AuthenticationService(userRepository);

    String testUsername = "testUsername";
    String testPassword = "testPassword";


    @Test
    void testConstructorNullUserRepositoryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AuthenticationService(null),
                "UserRepository cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorNotNullUserRepositoryDoesNotThrowException() {
        assertDoesNotThrow(() -> new AuthenticationService(userRepository),
                "UserRepository is not null and exception should not be thrown!");
    }

    @Test
    void testRegisterNullUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(null, testPassword),
                "Username cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testRegisterNullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(testUsername, null),
                "Password cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testRegisterUsernameExistsReturnsInstanceOfCommandResultWithNullNewUser(){
        when(userRepository.usernameExists(testUsername))
                .thenReturn(true);
        assertInstanceOf(CommandResult.class, authService.register(testUsername, testPassword),
                "It should return instance of CommandResult record!");

        assertEquals(null, authService.register(testUsername, testPassword).newUser());
    }

    @Test
    void testRegisterWhenAddNewUserThrowsIOExceptionThenReturnInstanceOfCommandResult() throws IOException {
        when(userRepository.usernameExists(testUsername)).thenReturn(false);

        doThrow(IOException.class)
                .when(userRepository)
                .addNewUser(eq(testUsername),any());

        assertInstanceOf(CommandResult.class, authService.register(testUsername, testPassword),
                "When adding new User and cannot add it should return CommandResult!");

        assertEquals(null, authService.register(testUsername, testPassword).newUser());
    }

    @Test
    void testRegisterValidAnythingReturnsInstanceOfCommandResultWithValidNewUser() throws IOException {
        when(userRepository.usernameExists(testUsername)).thenReturn(false);

        doNothing()
                .when(userRepository)
                .addNewUser(any(),any());

        assertInstanceOf(CommandResult.class, authService.register(testUsername, testPassword));
        assertEquals(testUsername, authService.register(testUsername, testPassword).newUser());
    }

    @Test
    void testLoginNullUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.login(null, testPassword),
                "Username cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testLoginNullPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.login(testUsername, null),
                "Password cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testLoginUsernameDoesNotExistReturnsInstanceOfCommandResultWithNullNewUser(){
        when(userRepository.usernameExists(testUsername)).thenReturn(false);
        assertInstanceOf(CommandResult.class, authService.login(testUsername, testPassword));
        assertEquals(null, authService.login(testUsername, testPassword).newUser());
    }

    @Test
    void testLoginInvalidPasswordReturnInstanceOfCommandResultWithNullNewUser(){
        when(userRepository.usernameExists(testUsername)).thenReturn(true);
        when(userRepository.getHashedPassword(testUsername)).thenReturn("Invalid Password");

        assertInstanceOf(CommandResult.class, authService.login(testUsername, testPassword));
        assertEquals(null, authService.login(testUsername, testPassword).newUser());
    }

    @Test
    void testLoginReturnsInstanceOfCommandResultWithValidNewUser() {
        String hashed = Sha256Hashing.hashing(testPassword);

        when(userRepository.usernameExists(testUsername)).thenReturn(true);
        when(userRepository.getHashedPassword(testUsername)).thenReturn(hashed);

        assertInstanceOf(CommandResult.class, authService.login(testUsername, testPassword));
        assertEquals(testUsername, authService.login(testUsername, testPassword).newUser());

    }
}
