package org.koenighotze.jee7hotel.guest.frontend;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dschmitz
 */
@Path("messages")
public class Messages {

    @GET
    @Produces(APPLICATION_JSON)
    public List<Message> getMessageBundle() {

        try {
            Properties properties = new Properties();
            properties.load(Messages.class.getResourceAsStream("/org/koenighotze/jee7hotel/guest/messages.properties"));

            List<Message> collect = properties.keySet().stream()
                    .map(k -> (String) k)
                    .map(key -> new Message(key, properties.getProperty(key)))
                    .collect(toList());

            return collect;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
