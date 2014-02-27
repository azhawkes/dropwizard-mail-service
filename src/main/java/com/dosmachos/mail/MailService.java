package com.dosmachos.mail;

import com.dosmachos.mail.core.MailMessageDTO;
import com.dosmachos.mail.domain.MailMessage;
import com.dosmachos.mail.backend.MailMessenger;
import com.dosmachos.mail.health.DatabaseHealthCheck;
import com.dosmachos.mail.health.MongoHealthCheck;
import com.dosmachos.mail.resources.MailMessagesResource;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import net.vz.mongodb.jackson.JacksonDBCollection;

/**
 * The transactional mail service.
 */
public class MailService extends Service<MailServiceConfiguration> {
    @Override
    public void initialize(Bootstrap<MailServiceConfiguration> bootstrap) {
        bootstrap.setName("mail");
    }

    @Override
    public void run(MailServiceConfiguration configuration, Environment environment) throws Exception {
        Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
        MongoManaged mongoManaged = new MongoManaged(mongo);

        environment.manage(mongoManaged);

        environment.addHealthCheck(new MongoHealthCheck(mongo));
        environment.addHealthCheck(new DatabaseHealthCheck());


        DB db = mongo.getDB(configuration.mongodb);
        JacksonDBCollection<MailMessage, String> mailMessages = JacksonDBCollection.wrap(db.getCollection("mailMessages"), MailMessage.class, String.class);

        environment.addResource(new MailMessagesResource(mailMessages));

        new MailMessenger(mailMessages, configuration.smtpHost, configuration.smtpPort).start();
    }

    public static void main(String[] args) throws Exception {
        new MailService().run(args);
    }

}
