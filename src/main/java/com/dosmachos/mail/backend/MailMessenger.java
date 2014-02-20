package com.dosmachos.mail.backend;

import com.dosmachos.mail.domain.MailMessage;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;
import org.bson.types.ObjectId;
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
    private Thread thread;
    private volatile boolean running = false;

    public MailMessenger(JacksonDBCollection<MailMessage, String> mailMessages) {
        this.mailMessages = mailMessages;
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

                log.info("Next queued message: " + message);

                if (message != null) {
                    try {
                        passToBackendMailServer(message);

                        log.info("Processed message " + message.getId());
                    } catch (Exception e) {
                        log.warn("Unable to process message " + message.getId() + "! Leaving it in the queue.", e);
                    }

                    Thread.sleep(SLEEP_MS);
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
        DBCursor<MailMessage> cursor = mailMessages.find().is("status", "new");

        // TODO - hold onto the cursor and reuse it?
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return null;
        }
    }

    private void passToBackendMailServer(MailMessage message) {
        // TODO
        message.setStatus("processed");

        WriteResult<MailMessage,String> result = mailMessages.updateById(message.getId(), message);

        System.out.println("*** RESULT: " + result.getN());

        DBCursor<MailMessage> processed = mailMessages.find().is("status", "processed");

        if (processed.hasNext()) {
            System.out.println("**** PROCESSED ONE!");
        }


//        throw new RuntimeException("there is no backend mail server yet!");

        // TODO - if it is successfully queued in the back-end, change the status in our database
    }
}
