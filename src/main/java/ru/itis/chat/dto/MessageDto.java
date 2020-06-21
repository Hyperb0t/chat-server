package ru.itis.chat.dto;

import ru.itis.chat.models.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageDto implements Dto {
    private Message message;
    private Target target;

    public MessageDto(Message message, Target target) {
        this.message = message;
        this.target = target;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public Map<String, String> getPayload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("id", message.getId());
        payload.put("text", message.getText());
        return payload;
    }

    @Override
    public String getHeader() {
        return "Message";
    }

    public enum Target {
        ALL,
        THIS
    }
}
