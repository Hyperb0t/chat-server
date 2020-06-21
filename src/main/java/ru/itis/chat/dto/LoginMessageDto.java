package ru.itis.chat.dto;

import ru.itis.chat.models.Message;

import java.util.HashMap;
import java.util.Map;

public class LoginMessageDto implements Dto{
    private Message message;
    private String jwt;

    public LoginMessageDto() {
    }

    public LoginMessageDto(Message message, String jwt) {
        this.message = message;
        this.jwt = jwt;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Map<String, String> getPayload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("id", message.getId());
        payload.put("text", message.getText());
        payload.put("jwt", jwt);
        return payload;
    }

    @Override
    public String getHeader() {
        return "Message";
    }

    @Override
    public String toString() {
        return "LoginMessageDto{" +
                "message=" + message +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
