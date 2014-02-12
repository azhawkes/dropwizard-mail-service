package com.dosmachos.mail.core;

/**
 * Representation of a mail message. Instances of this class are supposed to be immutable (I think this is to underscore
 * the fact that they are JackSON fodder and nothing more).
 */
public class MailMessage {
    private final String id;
    private final String to;
    private final String from;
    private final String body;

    public MailMessage(String id, String to, String from, String body) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }
}
