package ru.itis.chat.dao;

import ru.itis.chat.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    public List<Product> getAllProducts();
    public void addProduct(String name, int price, String description);
    public void addProductToUser(int productId, int userId);
    public Optional<Product> find(Integer id);
}
