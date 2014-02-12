package com.dosmachos.mail;

import com.dosmachos.mail.backend.MailMessenger;
import com.dosmachos.mail.health.DatabaseHealthCheck;
import com.dosmachos.mail.resources.MailMessagesResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

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
        environment.addResource(new MailMessagesResource());

        environment.addHealthCheck(new DatabaseHealthCheck());

        new MailMessenger().start();
    }

    public static void main(String[] args) throws Exception {
        new MailService().run(args);
    }

}
