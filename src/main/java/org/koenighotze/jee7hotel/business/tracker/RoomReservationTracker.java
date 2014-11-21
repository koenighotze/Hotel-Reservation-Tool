

package org.koenighotze.jee7hotel.business.tracker;

import org.koenighotze.jee7hotel.business.events.ReservationStatusChangeEvent;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample class demonstrating WebSocket integration
 * 
 * @author dschmitz
 */
@Singleton
@ServerEndpoint("/booking/tracking")
public class RoomReservationTracker {
    private static final Logger LOGGER = Logger.getLogger(RoomReservationTracker.class.getName());
    
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        LOGGER.log(Level.INFO, "add session {0}", session.getId());
        sessions.add(session);
    }

    @OnClose
    public void onClose(final Session session) {
        LOGGER.log(Level.INFO, "close session {0}", session.getId());
        sessions.remove(session);
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.log(Level.INFO, "Shutting down...");
        this.sessions
                .stream()
                .forEach(session -> closeAndRemoveSession(session));
    }

    private void closeAndRemoveSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,
                    "Cannot close session " + session.getId(), e);
        }
        onClose(session);
    }


    public void onReservationStateChange(@Observes ReservationStatusChangeEvent reservationStateChange) {
        Writer writer = new StringWriter();

        try (JsonGenerator generator = Json.createGenerator(writer)) {
            generator
                    .writeStartObject()
                    .write("reservationNumber", reservationStateChange.getReservationNumber())
                    .write("oldState", "null")
                    // TODO: this breaks the translation of enum to string and should be modified
                    .write("newState", reservationStateChange.getNewState().name())
                    .writeEnd();
        }

        String jsonValue = writer.toString();

        this.sessions.forEach(session -> sendDataToSession(session, jsonValue));
    }

    private void sendDataToSession(Session session, String jsonValue) {
        LOGGER.log(Level.INFO, "sending data to session {0}", session.getId());
        try {
            session.getBasicRemote().sendText(jsonValue);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING,
                    "Cannot handle " + jsonValue + " for session " + session.getId(), ex);
        }
    }
}
