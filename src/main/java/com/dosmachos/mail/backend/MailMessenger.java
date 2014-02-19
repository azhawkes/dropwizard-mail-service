package com.dosmachos.mail.backend;

import com.dosmachos.mail.domain.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Daemon thread worker that looks for messages in the database queue and hands them off to an SMTP server.
 * This could certainly be more sophisticated but being a simple man I like simple things.
 */
public class MailMessenger implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MailMessenger.class);

    private static final long SLEEP_MS = 10000;

    private Thread thread;
    private volatile boolean running = false;

    public MailMessenger() {
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
                MailMessage message = getNextQueuedMessage();

                if (message != null) {
                    try {
                        passToBackendMailServer(message);

                        log.info("Processed message " + message.get_id());
                    } catch (Exception e) {
                        log.warn("Unable to process message " + message.get_id() + "! Leaving it in the queue.");
                    }
                } else {
                    log.info("MailMessenger has nothing to do! Zzzzzzzz...");

                    Thread.sleep(SLEEP_MS);
                }
            } catch (InterruptedException e) {
                running = false;
            }
        }

        log.info("MailMessenger is exiting.");
    }

    private MailMessage getNextQueuedMessage() {
        // TODO
//        return new MailMessage(UUID.randomUUID().toString(), "recipient@gmail.com", "sender@gmail.com", "Howdy partna");
        return null;
    }

    private void passToBackendMailServer(MailMessage message) {
        // TODO
        throw new RuntimeException("there is no backend mail server yet!");

        // TODO - if it is successfully queued in the back-end, change the status in our database
    }
}
