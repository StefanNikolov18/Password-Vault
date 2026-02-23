package password.vault.server.command;

import org.junit.jupiter.api.Test;
import password.vault.server.command.unauthenticated.LoginCommand;
import password.vault.server.service.auth.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginCommandTest {

    AuthenticationService authenticationService = mock(AuthenticationService.class);
    LoginCommand loginCommand = new LoginCommand(authenticationService);

    String[] goodArgs = {"test", "test"};
    String[] badArgs = {"test"};

    @Test
    void testConstructorAuthenticationServiceNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoginCommand(null),
                "AuthenticationService cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorAuthenticationServiceNotNullDoesNotThrowsException() {
        assertDoesNotThrow(() ->  new LoginCommand(authenticationService),
                "AuthenticationService is not NULL and exception should not be thrown!");
    }

    @Test
    void testExecuteArgsNullShouldThrowException(){
        assertThrows(IllegalArgumentException.class,
                () -> loginCommand.execute(null, null),
                "Arguments cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteNotNullCurrentUserReturnsInstanceOfCommandResultWithNullNewUser(){
        assertInstanceOf(CommandResult.class,
                loginCommand.execute(goodArgs, "currentUserTest"),
                "Instance of CommandResult should be returned!");

        assertEquals(null , loginCommand.execute(goodArgs, "test").newUser());
    }

    @Test
    void testExecuteNotValidArgsCountShouldReturnInstanceOfCommandResult(){
        assertInstanceOf(CommandResult.class,
                loginCommand.execute(badArgs, null),
                "Instance of CommandResult should be returned!");
    }

    @Test
    void testExecuteValidArgumentsDoesNotThrowException(){
        when(authenticationService.login(goodArgs[0],goodArgs[1]))
                .thenReturn(new CommandResult("testUser", "testPassword"));

        assertDoesNotThrow(() -> loginCommand.execute(goodArgs, null),
                "Valid arguments should not throw exception!");
    }

}
