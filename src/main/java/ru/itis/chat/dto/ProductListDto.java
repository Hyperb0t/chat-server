package ru.itis.chat.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.chat.models.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListDto implements Dto {
    private List<Product> products;

    public ProductListDto() {
    }

    public ProductListDto(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public Map<String, String> getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> payload = new HashMap<>();
        try {
            payload.put("data", mapper.writeValueAsString(products));
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return payload;
    }

    @Override
    public String getHeader() {
        return "ProductList";
    }
}
