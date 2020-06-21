package ru.itis.chat.dao;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ConnectionComponent;
import ru.itis.chat.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements Component, UserRepository {
    private Connection connection;

    public UserRepositoryImpl(ConnectionComponent connectionComponent) {
        this.connection = connectionComponent.getConnection();
    }

    public Optional<User> getUser(String login) {
        try {
            PreparedStatement st = connection.prepareStatement
                    ("SELECT * FROM public.\"Users\"" +
                            "WHERE \"Login\"=?" +
                            "LIMIT 1");
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                return Optional.ofNullable(new User(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4)));
            }else {
                return Optional.empty();
            }
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<String> checkPassword(String login) {
        try {
            PreparedStatement st = connection.prepareStatement
                    ("SELECT \"Password\" FROM public.\"Users\"" +
                            "WHERE \"Login\"=?" +
                            "LIMIT 1");
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            rs.next();
            String result;
            try {
                result = rs.getString(1);
            } catch (Exception e) {
                return Optional.empty();
            }
            return Optional.ofNullable(result);
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void addUser(User user) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
