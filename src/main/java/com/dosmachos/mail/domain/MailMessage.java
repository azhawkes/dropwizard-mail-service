package com.dosmachos.mail.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.vz.mongodb.jackson.Id;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MailMessage {

    @Id
    private String _id;
    @NotBlank
    private String status = "new";
    @Email
    @NotBlank
    private String to = null;
    @Email
    @NotBlank
    private String from = null;
    private String subject;
    private String body = null;

    private final Date dateCreated = new Date();

    public MailMessage() {

    }

    public MailMessage(String subject, String to, String from, String body) {
        super();
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.body = body;
    }

    public String get_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Date getDateCreated() {
        return dateCreated;
    }
}
