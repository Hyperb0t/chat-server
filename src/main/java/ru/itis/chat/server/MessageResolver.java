package ru.itis.chat.server;

import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;

import java.util.Map;

public interface MessageResolver {
    public Dto resolve(JavalabProtocolPacket command, Map<String, String> jwtData, ClientHandler thisHandler,
                       ServerContext context);
    public Dto executeWelcomeService(ClientHandler thisHandler,
                                     ServerContext context);
}
