package ru.itis.chat.dao;

import ru.itis.chat.models.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    public void writeMessage(int userId, String message);
    public List<Message> getMessagePage(int size, int page);
    public List<Message> findAll();
    public Optional<Message> find(Integer id);
}
