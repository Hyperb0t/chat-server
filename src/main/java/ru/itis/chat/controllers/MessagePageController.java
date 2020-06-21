package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.services.MessageService;

import java.util.Map;

public class MessagePageController extends AbstractController implements Component {

    public MessagePageController() {
        this.commandName = "get messages";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        return context.getComponent(MessageService.class).getMessagePage(context, Integer.parseInt(inputPacket.get("size")),
                Integer.parseInt(inputPacket.get("page")));
    }
}
