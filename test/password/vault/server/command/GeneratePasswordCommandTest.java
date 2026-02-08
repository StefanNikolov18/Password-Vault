package password.vault.server.command;

import org.junit.jupiter.api.Test;
import password.vault.server.service.vault.VaultService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeneratePasswordCommandTest {
    VaultService vaultService = mock(VaultService.class);
    GeneratePasswordCommand generatePasswordCommand = new GeneratePasswordCommand(vaultService);

    String[] testArgsGood =  new String[]{"test", "test"};
    String[] testArgsBad = new String[]{"test"};

    String testCurrentUser = "testUser";

    @Test
    void testConstructorVaultServiceNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new GeneratePasswordCommand(null),
                "Vault Service cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testConstructorVaultServiceNotNullDoesNotThrowsException() {
        assertDoesNotThrow(() -> new GeneratePasswordCommand(vaultService),
                "Vault Service is not null and exception should not be thrown!");
    }

    @Test
    void testExecuteNullArgsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> generatePasswordCommand.execute(null, testCurrentUser),
                "Args cannot be null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testExecuteNullCurrentUserReturnsCommandResultRecordWithNullNewUser(){
        assertInstanceOf(CommandResult.class,
                generatePasswordCommand.execute(testArgsGood, null),
                "Current user in not logged in and CommandResult record should be return!");

        assertEquals(null,generatePasswordCommand.execute(testArgsGood, null).newUser());

    }

    @Test
    void testExecuteInvalidCountArgsReturnsCommandResultRecordWithMessage(){
        assertInstanceOf(CommandResult.class,
                generatePasswordCommand.execute(testArgsBad, testCurrentUser),
                "Args are not needed count and instance of CommandResult should be returned!");
    }

    @Test
    void testExecuteValidParamsShouldNotThrowsException(){
        when(vaultService.generatePassword(testArgsGood[0],testArgsGood[1],testCurrentUser))
                .thenReturn(new CommandResult(testCurrentUser, "testMessage!"));

        assertDoesNotThrow(() -> generatePasswordCommand.execute(testArgsGood, testCurrentUser),
                "Args are not null and full and exception should not be thrown!");
    }

}
