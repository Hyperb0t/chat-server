package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.services.ProductService;

import java.util.Map;

public class GetProductsController extends AbstractController implements Component {

    public GetProductsController() {
        this.commandName = "get products";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {

        return context.getComponent(ProductService.class).getAllProduct(JwtData, context);
    }
}
