package ru.itis.chat.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.chat.models.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePageDto implements Dto {
    private List<Message> messages;

    public MessagePageDto() {
    }

    public MessagePageDto(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public Map<String, String> getPayload() {
        Map<String, String> payload = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            payload.put("data", mapper.writeValueAsString(messages));
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return payload;
    }

    @Override
    public String getHeader() {
        return "MessagePage";
    }
}
