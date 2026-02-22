package password.vault.server.service.vault;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import password.vault.server.algorithm.cipher.SymmetricBlockCipher;
import password.vault.server.command.CommandResult;
import password.vault.server.exception.CipherException;
import password.vault.server.exception.EnzoicPasswordClientException;
import password.vault.server.integration.enzoic.EnzoicPasswordClient;
import password.vault.server.integration.enzoic.EnzoicPasswordResponse;
import password.vault.server.algorithm.PasswordGenerator;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaultServiceTest {
    SymmetricBlockCipher cipher = mock(SymmetricBlockCipher.class);
    VaultRepository vaultRepo = mock(VaultRepository.class);
    EnzoicPasswordClient enzoicClient = mock(EnzoicPasswordClient.class);

    VaultService vaultService = new VaultService(vaultRepo, enzoicClient, cipher);


    @Test
    void testConstructorKeyFailToLoadThrowsException() {
        try (MockedStatic<SecretKeyLoaderSingleton> mockedLoader =
                     mockStatic(SecretKeyLoaderSingleton.class)) {

            mockedLoader.when(SecretKeyLoaderSingleton::getInstance)
                    .thenThrow(new IOException("File not found"));

            assertThrows(IOException.class, () -> new VaultService());
        }
    }

    @Test
    void testConstructorDoesNotThrowException() {
        assertDoesNotThrow(() -> new VaultService(vaultRepo, enzoicClient, cipher),
        "Should not throw Exception!");
    }

    String testWebsite = "testWebsite";
    String testUsernameWebsite = "testUsername";
    String testPasswordWebsite = "testPassword";
    String testUser = "testPassword";

    @Test
    void testRetrieveCredentialsNullArgumentsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> vaultService.retrieveCredentials(null, null, null),
                "Arguments cannot be null and IllegalArgumentException should been thrown!");
    }

    @Test
    void testRetrieveCredentialsVaultRepoGetDecryptedPasswordForWebsideThrowsException() throws IOException {
        doThrow(IOException.class).when(vaultRepo)
        .getDecryptedPasswordForWebsite(testWebsite, testUsernameWebsite, testUser);

        assertThrows(RuntimeException.class,
                () ->  vaultService.retrieveCredentials(testWebsite, testUsernameWebsite, testUser),
                "It should throw RuntimeException!");

    }

    @Test
    void testRetrieveCredentialsVaultRepoGetEncryptedPasswordForWebsiteWhenNotSuccessfulReturnInstanceOfVaultResponse()
            throws IOException {
        when(vaultRepo.getDecryptedPasswordForWebsite(testWebsite, testUsernameWebsite, testUser))
                .thenReturn(new VaultResponse(false, "ädsasd"));

        assertInstanceOf(CommandResult.class,
                vaultService.retrieveCredentials(testWebsite, testUsernameWebsite, testUser),
                "Command Result with error message!");
    }

    @Test
    void testRetrieveCredentialSuccessfulReturnsInstanceOfCommandResult() throws IOException {
        when(vaultRepo.getDecryptedPasswordForWebsite(testWebsite, testUsernameWebsite, testUser))
                .thenReturn(new VaultResponse(true, testUsernameWebsite));

        assertInstanceOf(CommandResult.class,
                vaultService.retrieveCredentials(testWebsite, testUsernameWebsite, testUser),
                "It should return CommandResult");
    }

    @Test
    void testAddPasswordNullArgsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> vaultService.addPassword(null,
                        null,
                        null,
                        null),
                "Argument cannot be null in add Password and IllegalArgumentException should been thrown!");
    }

    @Test
    void testAddPasswordEnzoicClientThrowsException() throws EnzoicPasswordClientException {
        doThrow(EnzoicPasswordClientException.class).when(enzoicClient)
                        .getResponse(testPasswordWebsite);

        assertThrows(RuntimeException.class,
                () -> vaultService.addPassword(testWebsite,
                        testUsernameWebsite,
                        testPasswordWebsite,
                        testUser),
                "Enzoic Client throws Exception and shoudl return RuntimeException!");
    }

    @Test
    void testAddPasswordEnzoicClientReturnsIsRevealedInExposureReturnInstanceOfCommandResult()
            throws EnzoicPasswordClientException {
        when(enzoicClient.getResponse(testPasswordWebsite))
                .thenReturn(new EnzoicPasswordResponse(
                        true,
                        0,
                        0));

        assertInstanceOf(CommandResult.class,
                vaultService.addPassword(testWebsite,
                        testUsernameWebsite,
                        testPasswordWebsite,
                        testUser),
                "It is revealed in exposure and should return instance of CommandResult!");
    }

    @Test
    void testAddPasswordSuccessfulReturnInstanceOfCommandResult()
            throws
            EnzoicPasswordClientException,
            CipherException, IOException {
        when(enzoicClient.getResponse(testPasswordWebsite))
                .thenReturn(new EnzoicPasswordResponse(false,
                        0 ,
                        0));
        when(cipher.encrypt(testPasswordWebsite)).thenReturn("lele");
        when(vaultRepo.addNewPasswordForWebsite(testWebsite,
                testUsernameWebsite,
                "lele",
                testUser))
                .thenReturn(new VaultResponse(true, "hihi"));

        assertInstanceOf(CommandResult.class,
                vaultService.addPassword(testWebsite,
                        testUsernameWebsite,
                        testPasswordWebsite,testUser),
                "It should return CommandResult!");
    }

    @Test
    void testGeneratePasswordNullArgsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> vaultService.generatePassword(null, null, testUser),
                "Nul args should throw Exception!");
    }

    @Test
    void testGeneratePasswordGenPasswordThrowException(){
        try (MockedStatic<PasswordGenerator> mockedGenerator = mockStatic(PasswordGenerator.class)) {
            mockedGenerator.when(() -> PasswordGenerator.generatePassword(enzoicClient))
                    .thenThrow(new EnzoicPasswordClientException("Fail"));

            assertThrows(RuntimeException.class,
                    () -> vaultService.generatePassword(testWebsite,
                            testUsernameWebsite,
                            testUser),
                    "Should throw reutime exception when generate password failed!");
        }
    }

    @Test
    void testGeneratePasswordSuccessReturnsInstanceOfCommandResult(){
        try (MockedStatic<PasswordGenerator> mockedGenerator = mockStatic(PasswordGenerator.class)) {
            mockedGenerator.when(() -> PasswordGenerator.generatePassword(enzoicClient))
                    .thenReturn("password");

            when(cipher.encrypt(any())).thenReturn("password");

            when(vaultRepo.addNewPasswordForWebsite(any(),any(), any(),any()))
                            .thenReturn(new VaultResponse(true, "test"));

            assertInstanceOf(CommandResult.class,
                    vaultService.generatePassword(testWebsite,
                            testUsernameWebsite,
                            testUser),
                    "It should return CommandResult!"
                    );

        } catch (CipherException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRemovePasswordNullArgsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> vaultService.removePassword(null, null, null),
                "Argument are null and IllegalArgumentException should be thrown!");
    }

    @Test
    void testRemovePasswordVaultRepoRemovePasswordThrowsException()
            throws IOException {
        when(vaultRepo.removePassword(any(), any(), any()))
                .thenThrow(IOException.class);

        assertThrows(RuntimeException.class,
                () -> vaultService.removePassword(testWebsite, testUsernameWebsite, testUser),
                "Remove Password in Vault Repo throws Exception!");
    }

    @Test
    void testRemovePasswordSuccessfulReturnsInstanceOfCommandResult() throws IOException {
        when(vaultRepo.removePassword(any(), any(), any()))
                .thenReturn(new VaultResponse(true, "testMessage"));

        assertInstanceOf(CommandResult.class,
                vaultService.removePassword(testWebsite, testUsernameWebsite, testUser),
                "It should return CommandResult!");
    }
}
