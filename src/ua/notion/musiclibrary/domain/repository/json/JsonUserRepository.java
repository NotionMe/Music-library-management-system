package ua.notion.musiclibrary.domain.repository.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.reflect.TypeToken;

import ua.notion.musiclibrary.domain.enums.Role;
import ua.notion.musiclibrary.domain.model.User;
import ua.notion.musiclibrary.domain.repository.UserRepository;

public class JsonUserRepository extends CachedJsonRepository<User, UUID> implements UserRepository {

    public JsonUserRepository(String filename) {
        super(
                filename,
                new TypeToken<ArrayList<User>>() {
                }.getType(),
                User::getID);
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
