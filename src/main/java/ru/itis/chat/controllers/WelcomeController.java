package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.models.Message;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.config.ServerContext;

import java.util.Map;

public class WelcomeController extends AbstractController implements Component {

    public WelcomeController() {
        this.commandName = "Welcome";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        return new MessageDto(new Message("server", "Welcome! Please log in"), MessageDto.Target.THIS);
    }

}
