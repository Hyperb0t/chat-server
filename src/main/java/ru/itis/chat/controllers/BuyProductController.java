package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.services.ProductService;

import java.util.Map;

public class BuyProductController extends AbstractController implements Component {

    public BuyProductController() {
        this.commandName = "buy product";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        return context.getComponent(ProductService.class).addProductToUser(Integer.parseInt(inputPacket.get("product id")),
               JwtData, context );
    }
}
