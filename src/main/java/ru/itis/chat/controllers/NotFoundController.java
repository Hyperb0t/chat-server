package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.models.Message;
import ru.itis.chat.server.JavalabProtocolPacket;

import java.util.Map;

public class NotFoundController extends AbstractController implements Component {

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        //System.out.println("unknown command from " + thisHandler.getClientSocket().getInetAddress().getHostAddress());
        return new MessageDto(new Message("server", "unknown command"), MessageDto.Target.THIS);
    }
}
