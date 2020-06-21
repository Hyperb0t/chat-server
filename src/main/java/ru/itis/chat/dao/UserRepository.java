package ru.itis.chat.dao;

import ru.itis.chat.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<User> getUser(String login);
    public Optional<String> checkPassword(String login);
    public void addUser(User user);
    public List<User> findAll();
}
