package ru.itis.chat.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.chat.config.Component;
import ru.itis.chat.config.ConnectionComponent;
import ru.itis.chat.models.Message;
import ru.itis.chat.server.JavalabProtocolPacket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

public class MessageRepositoryImpl implements Component, MessageRepository {
    private Connection connection;

//    public MessageRepository(Connection connection) {
//        this.connection = connection;
//    }

    public MessageRepositoryImpl(ConnectionComponent connectionComponent) {
        this.connection = connectionComponent.getConnection();
    }

    public void writeMessage(int userId, String message) {
        try {
            PreparedStatement st = connection.prepareStatement
                    ("INSERT INTO public.\"Messages\" " +
                            "(\"User_id\", \"Content\", \"DateTime\")" +
                            "VALUES (?,?,?)");
            st.setInt(1, userId);
            st.setString(2, message);
            st.setTimestamp(3, Timestamp.from(Instant.now()));
            st.execute();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Message> getMessagePage(int size, int page) {
        List<Message> messages = new LinkedList<>();
        try {
            PreparedStatement st = connection.prepareStatement
                    ("SELECT public.\"Users\".\"Login\"," +
                            " public.\"Messages\".\"Content\"," +
                            " public.\"Messages\".\"DateTime\" FROM public.\"Messages\"" +
                            "JOIN public.\"Users\" ON " +
                            "public.\"Messages\".\"User_id\" = public.\"Users\".\"Id\"" +
                            "ORDER BY \"DateTime\" DESC " +
                            "LIMIT " + size + " OFFSET " +  (page-1)*size);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String login = rs.getString(1);
                String message = rs.getString(2);
                Timestamp time = rs.getTimestamp(3);
                Message oneMessage = new Message(login, message, time.toString());
                messages.add(0, oneMessage);
            }
            return messages;
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    public List<Message> findAll() {
        return null;
    }

    @Override
    public Optional<Message> find(Integer id) {
        return Optional.empty();
    }
}
