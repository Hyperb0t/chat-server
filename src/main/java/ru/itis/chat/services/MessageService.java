package ru.itis.chat.services;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dao.MessageRepository;
import ru.itis.chat.dao.MessageRepositoryImpl;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.dto.MessagePageDto;
import ru.itis.chat.models.Message;

import java.util.Map;

public class MessageService implements Component {
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Dto sendMessage(ServerContext context, Map<String, String> JwtData, String text) {
        if (JwtData != null) {
            context.getComponent(MessageRepositoryImpl.class).writeMessage(Integer.parseInt(JwtData.get("id")), text);
            return new MessageDto(new Message(JwtData.get("login"), text), MessageDto.Target.ALL);
        } else {
            return new MessageDto(new Message("server", "you are not logged in to send messages"),
                    MessageDto.Target.THIS);
        }
    }

    public Dto getMessagePage (ServerContext context, Integer size, Integer page) {
        return new MessagePageDto(context.getComponent(MessageRepositoryImpl.class).getMessagePage
                (size, page));
    }
}
