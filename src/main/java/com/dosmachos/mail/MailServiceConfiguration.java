package com.dosmachos.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Service configuration for our Dropwizard mail service.
 */
public class MailServiceConfiguration extends Configuration {
    @JsonProperty
    @NotEmpty
    public String mongohost = "localhost";

    @JsonProperty
    @Min(1)
    @Max(65535)
    public int mongoport = 27017;

    @JsonProperty
    @NotEmpty
    public String mongodb = "mailService";

    @JsonProperty
    @NotEmpty
    public String smtpHost = "localhost";

    @JsonProperty
    @Min(1)
    @Max(65535)
    public int smtpPort = 25;
}
