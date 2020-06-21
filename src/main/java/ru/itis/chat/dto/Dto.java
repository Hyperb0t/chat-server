package ru.itis.chat.dto;

import java.util.Map;

public interface Dto {
    public Map<String, String> getPayload();
    public String getHeader();
}
