package com.dosmachos.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Service configuration for our Dropwizard mail service.
 */
public class MailServiceConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String foo;

    public String getFoo() {
        return foo;
    }
}
