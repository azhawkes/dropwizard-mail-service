package com.dosmachos.mail.resources;

import com.dosmachos.mail.core.MailMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * Root resource for mail messages (listing and creating).
 */
@Path("/mail/messages")
@Produces(MediaType.APPLICATION_JSON)
public class MailMessagesResource {
    public MailMessagesResource() {
    }

    @GET
    public MailMessage[] list() {
        return new MailMessage[]{
                new MailMessage(UUID.randomUUID().toString(), "recipient@gmail.com", "sender@gmail.com", "Hello there"),
                new MailMessage(UUID.randomUUID().toString(), "recipient@gmail.com", "sender@gmail.com", "Why hello")
        };
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public MailMessage create(@FormParam("to") String to, @FormParam("from") String from, @FormParam("body") String body) {
        // TODO - make a real one in the database
        // TODO - maybe more appropriate to return a reference to the new message than the message itself
        return new MailMessage(UUID.randomUUID().toString(), to, from, body);
    }

    @GET
    @Path("/{id}")
    public MailMessage retrieve(@PathParam("id") String id) {
        // TODO - fetch it from the database instead of mocking it
        return new MailMessage(id, "recipient@gmail.com", "sender@gmail.com", "Hello");
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, @FormParam("to") String to, @FormParam("from") String from, @FormParam("body") String body) {
        // TODO - update it in the database
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        // TODO - delete it in the database
    }
}
