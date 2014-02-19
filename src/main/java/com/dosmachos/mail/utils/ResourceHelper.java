package com.dosmachos.mail.utils;

import net.vz.mongodb.jackson.DBCursor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourceHelper {
    public static void notFoundIfNull(DBCursor<?> cursor) {
        if (!cursor.hasNext()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    public static void notFoundIfNull(Object obj) {
        errorIfNull(obj, Response.Status.NOT_FOUND);
    }

    public static void errorIfNull(Object obj, Response.Status status) {
        if (obj == null) {
            throw new WebApplicationException(status);
        }
    }
}
