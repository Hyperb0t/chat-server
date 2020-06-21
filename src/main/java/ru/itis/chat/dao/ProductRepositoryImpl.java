package ru.itis.chat.dao;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ConnectionComponent;
import ru.itis.chat.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements Component, ProductRepository {
    private Connection connection;

    public ProductRepositoryImpl(ConnectionComponent connectionComponent) {
        this.connection = connectionComponent.getConnection();
    }

    public List<Product> getAllProducts() {
        List<Product> result = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement
                    ("SELECT * FROM public.\"Products\"");
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                Product p = new Product(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getInt(3));
                result.add(p);
            }
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    public void addProduct(String name, int price, String description) {
        try {
            PreparedStatement st = connection.prepareStatement
                    ("INSERT INTO public.\"Products\" (" +
                            "\"Name\", \"Price\", \"Description\")" +
                            "VALUES( ?,?,?)");
            st.setString(1, name);
            st.setInt(2, price);
            st.setString(3, description);
            st.executeUpdate();
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void addProductToUser(int productId, int userId){
        try {
            PreparedStatement st = connection.prepareStatement
                    ("INSERT INTO public.\"Products_to_users\" (" +
                            "\"Product_id\", \"User_id\")" +
                            "VALUES (?,?)");
            st.setInt(1, productId);
            st.setInt(2, userId);
            st.executeUpdate();
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Product> find(Integer id) {
        return Optional.empty();
    }
}
