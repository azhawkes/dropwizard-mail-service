package com.dosmachos.mail.resources;

import com.dosmachos.mail.core.MailMessageDTO;
import com.dosmachos.mail.domain.MailMessage;
import com.dosmachos.mail.utils.ResourceHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.yammer.metrics.annotation.Timed;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;
import org.bson.types.ObjectId;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Root resource for mail messages (listing and creating).
 */
@Path("/mail/messages")
@Produces(MediaType.APPLICATION_JSON)
public class MailMessagesResource {
    private JacksonDBCollection<MailMessage, String> collection;

    public MailMessagesResource(JacksonDBCollection<MailMessage, String> mailMessages) {
        this.collection = mailMessages;
    }

    @GET
    public List<MailMessageDTO> list() {
        DBCursor<MailMessage> dbCursor = collection.find();

        List<MailMessageDTO> mailMessageList = new ArrayList<>();

        while (dbCursor.hasNext()) {
            MailMessage savedObject = dbCursor.next();
            mailMessageList.add(new MailMessageDTO(savedObject));
        }

        return mailMessageList;

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Timed
    public MailMessageDTO create(@FormParam("subject") String subject,@FormParam("to") String to, @FormParam("from") String from, @FormParam("body") String body) {
        MailMessage message =  new MailMessage(subject, to, from, body);
        MailMessage savedObject = collection.insert(message).getSavedObject();
        return new MailMessageDTO(savedObject);
    }

    @GET
    @Path("/{id}")
    @Timed
    public MailMessageDTO retrieve(@PathParam("id") String id) {
        DBCursor<MailMessage> cursor = collection.find().is("_id", new ObjectId(id));

        ResourceHelper.notFoundIfNull(cursor);
        MailMessage savedObject =  cursor.next();
        return new MailMessageDTO(savedObject);
    }

    @PUT
    @Path("/{id}")
    @Timed
    public Response update(@PathParam("id") String id, @FormParam("subject") String subject, @FormParam("to") String to, @FormParam("from") String from, @FormParam("body") String body) {
        DBCursor<MailMessage> cursor = collection.find().is("_id", id);

        if (cursor.hasNext()) {
            MailMessage message =  cursor.next();
            message.setSubject(subject);
            message.setTo(to);
            message.setFrom(from);
            message.setBody(body);

            ResourceHelper.notFoundIfNull(message);
            //WriteResult<MailMessage,String> result =  collection.updateById(message.get_id(), DBUpdate.set("to", to).set("subject", subject).set("from", from).set("body", body));
            WriteResult<MailMessage,String> result =  collection.updateById(message.getId(), message);
            int affectedObjects = result.getWriteResult().getN();
            if (affectedObjects != 1) {
                return Response.serverError().build();
            }
            return Response.noContent().build();
        } else {
            //Could not find object with this Id
            return Response.serverError().build();
        }

    }

    @DELETE
    @Path("/{id}")
    @Timed
    public Response delete(@PathParam("id") String id) {
        DBObject query = new BasicDBObject("_id", id);
        WriteResult<MailMessage,String> result = collection.remove(query);
        int affectedObjects = result.getWriteResult().getN();

        if (affectedObjects != 1) {
            return Response.serverError().build();
        }
        return Response.noContent().build();
    }
}
