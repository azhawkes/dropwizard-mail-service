package com.dosmachos.mail.core;

import com.dosmachos.mail.domain.MailMessage;

/**
 * Representation of a mail message. Instances of this class are supposed to be immutable (I think this is to underscore
 * the fact that they are JackSON fodder and nothing more).
 */
public class MailMessageDTO {
    private final String id;
    private final String status;
    private final String subject;
    private final String to;
    private final String from;
    private final String body;

    public MailMessageDTO(String id, String status, String subject, String to, String from, String body) {
        this.id = id;
        this.status = status;
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
    }

    public MailMessageDTO(MailMessage mailMessage) {
        this.id = mailMessage.get_id();
        this.status = mailMessage.getStatus();
        this.subject = mailMessage.getSubject();
        this.to = mailMessage.getTo();
        this.from = mailMessage.getFrom();
        this.body = mailMessage.getBody();
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
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
