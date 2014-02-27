package com.dosmachos.mail.backend;

import com.dosmachos.mail.domain.MailMessage;
import com.mongodb.MongoException;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Daemon thread worker that looks for messages in the database queue and hands them off to an SMTP server.
 * This could certainly be more sophisticated but being a simple man I like simple things.
 */
public class MailMessenger implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MailMessenger.class);

    private static final long SLEEP_MS = 10000;

    private JacksonDBCollection<MailMessage, String> mailMessages;
    private String smtpHost;
    private int smtpPort;

    private Thread thread;
    private volatile boolean running = false;

    public MailMessenger(JacksonDBCollection<MailMessage, String> mailMessages, String smtpHost, int smtpPort) {
        this.mailMessages = mailMessages;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public void start() {
        thread = new Thread(this, "MailMessenger");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;

        log.info("MailMessenger is up and running!");

        while (running) {
            try {
                DBCursor<MailMessage> cursor = mailMessages.find().is("status", "new");

                if (!cursor.hasNext()) {
                    log.info("MailMessenger has nothing to do! Zzzzzzzz...");
                }

                while (cursor.hasNext()) {
                    MailMessage message = cursor.next();

                    try {
                        sendMessage(message);
                    } catch (MongoException e) {
                        log.error("Nasty database error while processing message " + message.getId() + "! Message status is [" + message.getStatus() + "].", e);
                    }
                }

                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                running = false;
            }
        }

        log.info("MailMessenger is exiting.");
    }

    private void sendMessage(MailMessage message) throws MongoException {
        try {
            log.info("Preparing to send message " + message.getId());

            message.setStatus("sending");
            mailMessages.updateById(message.getId(), message);

            SimpleEmail email = new SimpleEmail();

            email.setHostName(smtpHost);
            email.setSmtpPort(smtpPort);
            email.setFrom(message.getFrom());
            email.addTo(message.getTo());
            email.setSubject(message.getSubject());
            email.setMsg(message.getBody());
            email.addHeader("X-Infusionsoft-Message-ID", message.getId());
            email.send();

            message.setStatus("sent");
            mailMessages.updateById(message.getId(), message);

            log.info("Sent message " + message.getId());
        } catch (EmailException e) {
            log.warn("Unable to send message " + message.getId() + "! We'll try again later.", e);

            message.setStatus("new");
            mailMessages.updateById(message.getId(), message);
        }
    }
}
