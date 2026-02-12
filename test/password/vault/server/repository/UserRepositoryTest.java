package password.vault.server.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        String usersData = "ivan:hashed123\npesho:hashed456\n";
        InputStream inputStream =
                new ByteArrayInputStream(usersData.getBytes());

        userRepository = new UserRepository(inputStream);
    }

    @Test
    void testUsernameExistsWhenUserExists() {
        assertTrue(userRepository.usernameExists("ivan"));
    }

    @Test
    void testUsernameExistsWhenUserDoesNotExist() {
        assertFalse(userRepository.usernameExists("gosho"));
    }

    @Test
    void testGetHashedPasswordReturnsCorrectPassword() {

        assertEquals("hashed456", userRepository.getHashedPassword("pesho"));
    }

    @Test
    void testGetHashedPasswordWhenUserDoesNotExist() {
        String password = userRepository.getHashedPassword("unknown");

        assertNull(password);
    }

    @Test
    void testAddNewUserAddsUserToMemory() throws Exception {
        UserRepository realRepo = new UserRepository(
                new ByteArrayInputStream("".getBytes())
        );

        UserRepository spyRepo = spy(realRepo);

        // Спираме IO частта
        doNothing().when(spyRepo).saveInDataBase(any(), any());
        doNothing().when(spyRepo).createVaultFile(any());

        spyRepo.addNewUser("maria", "hashed789");

        assertTrue(spyRepo.usernameExists("maria"));
    }
}
