package password.vault.server.command;

import org.junit.jupiter.api.Test;
import password.vault.server.command.authenticated.AddPasswordCommand;
import password.vault.server.service.vault.VaultService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddPasswordCommandTest {

    VaultService vaultService = mock(VaultService.class);
    AddPasswordCommand addPasswordCommand = new AddPasswordCommand(vaultService);

    String[] testArgsBad =  new String[]{"test", "test"};
    String[] testArgsGood = new String[]{"test", "test", "test"};

    String testCurrentUser = "testUser";

    @Test
    void testConstructorVaultServiceNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AddPasswordCommand(null),
                "Vault Service cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorVaultServiceNotNullDoesNotThrowsException() {
        assertDoesNotThrow(() -> new AddPasswordCommand(vaultService),
                "Vault Service is not null and exception should not be thrown!");
    }

    @Test
    void testExecuteNullArgsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> addPasswordCommand.execute(null, testCurrentUser),
                "Args cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteNullCurrentUserReturnsCommandResultRecordWithNullNewUser(){
        assertInstanceOf(CommandResult.class,
                addPasswordCommand.execute(testArgsGood, null),
                "Current user in not logged in and CommandResult record should be return!");

        assertEquals(null,addPasswordCommand.execute(testArgsGood, null).newUser());

    }

    @Test
    void testExecuteInvalidCountArgsReturnsCommandResultRecordWithMessage(){
        assertInstanceOf(CommandResult.class,
                addPasswordCommand.execute(testArgsBad, testCurrentUser),
                "Args are not needed count and instance of CommandResult should be returned!");
    }

    @Test
    void testExecuteValidParamsShouldNotThrowsException(){
        when(vaultService.addPassword(testArgsGood[0],testArgsGood[1],testArgsGood[2],testCurrentUser))
                .thenReturn(new CommandResult(testCurrentUser, "testMessage!"));

        assertDoesNotThrow(() -> addPasswordCommand.execute(testArgsGood, testCurrentUser),
                "Args are not null and full and exception should not be thrown!");
    }

}

