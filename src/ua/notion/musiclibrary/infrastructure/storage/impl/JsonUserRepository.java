package ua.notion.musiclibrary.infrastructure.storage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.impl.User;
import ua.notion.musiclibrary.infrastructure.storage.contract.UserRepository;

class JsonUserRepository extends CachedJsonRepository<User> implements UserRepository {

    public JsonUserRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<User>>() {
                }.getType());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findFirstBy(user -> user.getUsername().equals(username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findFirstBy(user -> user.getEmail().equals(email));
    }

    @Override
    public List<User> findByRole(Role role) {
        return findBy(user -> user.getRole() == role);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findFirstBy(user -> user.getUsername().equals(username)).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findFirstBy(user -> user.getEmail().equals(email)).isPresent();
    }

    @Override
    public List<User> findByUsernameContainingIgnoreCase(String username) {
        return findBy(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()));
    }
}
