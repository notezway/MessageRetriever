package ru.ntzw.messageretriever;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private final UUID id;
    private final LocalDateTime time;
    private final String text;

    public Message(UUID id, LocalDateTime time, String text) {
        this.id = id;
        this.time = time;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
}
