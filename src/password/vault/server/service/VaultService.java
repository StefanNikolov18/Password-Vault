package password.vault.server.service;

import password.vault.server.repository.UserRepository;

// for add/retrieve/remove/generate passwords
public class VaultService {
    private UserRepository userRepository;

    public VaultService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
