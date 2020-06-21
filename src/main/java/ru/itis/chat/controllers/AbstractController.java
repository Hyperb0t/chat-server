package ru.itis.chat.controllers;

import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.ClientHandler;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.config.ServerContext;

import java.io.PrintWriter;
import java.util.Map;

public abstract class AbstractController {
    protected String commandName;
    protected String rolePermission; //TODO

    protected PrintWriter getThisWriter(ClientHandler thisHandler) {
        try {
            return new PrintWriter(thisHandler.getClientSocket().getOutputStream(), true);
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    public abstract Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                                ServerContext context);

    public String getCommandName() {
        return commandName;
    }
}
