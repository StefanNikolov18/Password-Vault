package password.vault.server.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import password.vault.server.command.CommandResult;
import password.vault.server.command.unauthenticated.LoginCommand;
import password.vault.server.repository.UserRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandHandlerTest {

    UserRepository userRepository = mock(UserRepository.class);
    CommandHandler cmdHandler;

    @BeforeEach
    void setUp() throws IOException {
        cmdHandler = new CommandHandler(userRepository);
    }

    @Test
    void testConstructorNullUserRepositoryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new CommandHandler(null),
                "UserRepository cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorNotNullUserRepositoryDoesNotThrowException() {
        assertDoesNotThrow(() -> new CommandHandler(userRepository),
                "User repository is not null and exception should not be thrown!");
    }

    @Test
    void testExecuteNullCommandLineThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> cmdHandler.execute(null),
                "CommandLine cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteBlankCommandLineThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> cmdHandler.execute("      "),
                "CommandLine cannot be blank and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteLogoutCommandWhenNotLoggedInReturnInstanceOfString(){
        assertInstanceOf(String.class,
                cmdHandler.execute("logout"),
                "logout");
    }

    @Test
    void testExecuteLogoutCommandWhenLoggedInReturnInstanceOfString(){
        cmdHandler.execute("testUser");
        assertInstanceOf(String.class, cmdHandler.execute("logout"),
                "logout");
    }

    @Test
    void testExecuteUnknownCommand(){
        assertInstanceOf(String.class,
                cmdHandler.execute("unknown command!"));
    }

    @Test
    void testExecuteLoginCommandWhenNotLoggedIn() throws IOException {
        CommandHandler spyHandler = spy(new CommandHandler(mock(UserRepository.class)));

        LoginCommand loginCmd = mock(LoginCommand.class);

        when(loginCmd.execute(any(), any()))
                .thenReturn(new CommandResult("user123", "login successful"));

        spyHandler.getCommands().put("login", loginCmd);
        spyHandler.execute("login user123 password");

        assertEquals("user123", spyHandler.getCurrentUser(),
                "handleLogin should set currentUser after login!");
    }

    @Test
    void testExecuteReturnInstanceOfString(){
        assertInstanceOf(String.class,
                cmdHandler.execute("login user password"));
    }

}
