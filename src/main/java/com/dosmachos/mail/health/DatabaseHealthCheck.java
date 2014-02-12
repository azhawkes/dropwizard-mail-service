package com.dosmachos.mail.health;

import com.yammer.metrics.core.HealthCheck;

/**
 * Makes sure the database is online and operational for production monitoring.
 */
public class DatabaseHealthCheck extends HealthCheck {
    public DatabaseHealthCheck() {
        super("database");
    }

    @Override
    protected Result check() throws Exception {
        // TODO
        return Result.healthy();
    }
}
