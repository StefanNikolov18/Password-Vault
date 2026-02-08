package password.vault.server.command;

import org.junit.jupiter.api.Test;
import password.vault.server.service.auth.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterCommandTest {
    AuthenticationService authenticationService = mock(AuthenticationService.class);
    RegisterCommand registerCommand = new RegisterCommand(authenticationService);

    String[] goodArgs = {"test", "test", "test"};
    String[] argsNotEqualPasswords = {"test", "pass123", "password"};
    String[] badArgs = {"test"};

    @Test
    void testConstructorAuthenticationServiceNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegisterCommand(null),
                "AuthenticationService cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorAuthenticationServiceNotNullDoesNotThrowsException() {
        assertDoesNotThrow(() ->  new RegisterCommand(authenticationService),
                "AuthenticationService is not NULL and exception should not be thrown!");
    }

    @Test
    void testExecuteArgsNullShouldThrowException(){
        assertThrows(IllegalArgumentException.class,
                () -> registerCommand.execute(null, null),
                "Arguments cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteNotNullCurrentUserReturnsInstanceOfCommandResultWithNullNewUser(){
        assertInstanceOf(CommandResult.class,
                registerCommand.execute(goodArgs, "currentUserTest"),
                "Instance of CommandResult should be returned!");

        assertEquals(null , registerCommand.execute(goodArgs, "test").newUser());
    }

    @Test
    void testExecuteNotValidArgsCountShouldReturnInstanceOfCommandResult(){
        assertInstanceOf(CommandResult.class,
                registerCommand.execute(badArgs, null),
                "Instance of CommandResult should be returned!");
    }

    @Test
    void testExecuteNotEqualsPasswordsShouldReturnInstanceOfCommandResult(){
        assertInstanceOf(CommandResult.class,
                registerCommand.execute(argsNotEqualPasswords, null),
                "Instance of CommandResult should be returned!");
    }

    @Test
    void testExecuteValidArgumentsDoesNotThrowException(){
        when(authenticationService.register(goodArgs[0],goodArgs[1]))
                .thenReturn(new CommandResult("testUser", "testPassword"));

        assertDoesNotThrow(() -> registerCommand.execute(goodArgs, null),
                "Valid arguments should not throw exception!");
    }
}
