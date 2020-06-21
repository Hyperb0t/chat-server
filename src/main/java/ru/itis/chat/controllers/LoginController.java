package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.services.AuthService;

import java.util.Map;

public class LoginController extends AbstractController implements Component {

    public LoginController() {
        this.commandName = "Login";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {
        return context.getComponent(AuthService.class).loginUser(context,inputPacket.get("email"), inputPacket.get("password"));
    }
}
