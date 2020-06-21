package ru.itis.chat.services;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dao.ProductRepository;
import ru.itis.chat.dao.ProductRepositoryImpl;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.dto.ProductListDto;
import ru.itis.chat.models.Message;
import ru.itis.chat.models.Product;

import java.util.Map;

public class ProductService implements Component {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Dto addProduct(Product product, ServerContext context, Map<String, String> JwtData) {
        Message response = new Message();
        response.setId("server");
        if(JwtData.get("role").equals("admin")) {
            context.getComponent(ProductRepositoryImpl.class).addProduct
                    (product.getName(), product.getPrice(), product.getDescription());
            response.setText("product " + product.getName() + " successfully added");
        }
        else {
            response.setText("product " + product.getName() + " successfully added");
        }
        return new MessageDto(response, MessageDto.Target.THIS);

    }

    public Dto addProductToUser(Integer productId, Map<String, String> JwtData, ServerContext context) {
        Message response = new Message();
        response.setId("server");
        if(JwtData != null) {
            context.getComponent(ProductRepositoryImpl.class).addProductToUser(productId,
                    Integer.parseInt(JwtData.get("id")));
            response.setText("you have successfully bought the product with id " + productId);
        }
        else {
            response.setText("you are not logged in to buy products");
        }
        return new MessageDto(response, MessageDto.Target.THIS);
    }

    public Dto getAllProduct(Map<String, String> JwtData, ServerContext context) {
        if(JwtData != null) {
            ProductListDto response = new ProductListDto();
            try {
                response.setProducts(context.getComponent(ProductRepositoryImpl.class).getAllProducts());
                return response; //productList field was called "data"
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        else {
            Message response = new Message();
            response.setId("server");
            response.setText("you are not logged in to watch products");
            return new MessageDto(response, MessageDto.Target.THIS);
        }

    }
}
