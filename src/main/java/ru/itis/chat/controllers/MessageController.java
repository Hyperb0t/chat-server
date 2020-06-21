package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.services.MessageService;

import java.util.Map;

public class MessageController extends AbstractController implements Component {

    public MessageController() {
        this.commandName = "Message";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        return context.getComponent(MessageService.class).sendMessage(context, JwtData, inputPacket.get("text"));
    }

}
