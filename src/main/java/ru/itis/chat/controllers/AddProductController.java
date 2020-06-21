package ru.itis.chat.controllers;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.models.Product;
import ru.itis.chat.server.JavalabProtocolPacket;
import ru.itis.chat.services.ProductService;

import java.util.Map;

public class AddProductController extends AbstractController implements Component {

    public AddProductController() {
        this.commandName = "add product";
    }

    @Override
    public Dto execute(JavalabProtocolPacket inputPacket, Map<String, String> JwtData,
                       ServerContext context) {
        Product p = new Product();
        p.setName(inputPacket.get("name"));
        p.setDescription(inputPacket.get("description"));
        p.setPrice(Integer.parseInt(inputPacket.get("price")));
        return context.getComponent(ProductService.class).addProduct(p,context,JwtData);
    }
}
