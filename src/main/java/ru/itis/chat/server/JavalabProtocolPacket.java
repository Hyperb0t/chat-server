package ru.itis.chat.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(as = JavalabProtocolPacket.class)
public class JavalabProtocolPacket {
    private String header;
    private Map<String, String> payload;


    public JavalabProtocolPacket() {
    }

    public JavalabProtocolPacket(String header) {
        this.header = header;
        payload = new HashMap<>();
    }

    public JavalabProtocolPacket(String header, Map<String, String> payload) {
        this.header = header;
        this.payload = payload;
    }

    public static JavalabProtocolPacket fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, JavalabProtocolPacket.class);
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void put(String key, String value) {
        payload.put(key,  value);
    }

    public String get(String key) {
        return payload.get(key);
    }

    public String getHeader() {
        return header;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
