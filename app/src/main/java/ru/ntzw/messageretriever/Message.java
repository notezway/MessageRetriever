package ru.ntzw.messageretriever;

import java.util.UUID;

class Message {

    private final UUID id;
    private final long time;
    private final String text;

    Message(UUID id, long time, String text) {
        this.id = id;
        this.time = time;
        this.text = text;
    }

    UUID getId() {
        return id;
    }

    long getTime() {
        return time;
    }

    String getText() {
        return text;
    }
}
