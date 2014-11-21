package org.koenighotze.jee7hotel.business;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message driven generator of reservations.
 *
 * Waits for incoming JMS messages and creates appropriate
 * Reservations
 *
 *
 * Created by dschmitz on 20.11.14.
 *
 *
 */
//@JMSDestinationDefinition(
//        name = "java:app/jee7hotel/reservationQueue",
//        interfaceName = "javax.jms.Queue",
//        destinationName = "reservationQueue"
//)
/* Activate the traffic-rar resource adapter from this MDB */
//@MessageDriven(
//        activationConfig = {
//            @ActivationConfigProperty(
//                    propertyName = "port",
//                    propertyValue = "4008"
//            )
//        }
//)
// TODO activationSpec
// TODO Trigger Event
public class ReservationGenerationBean implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(ReservationGenerationBean.class.getName());

    @Override
    public void onMessage(Message message) {
            LOGGER.info(() -> "Received message " + message);

            LOGGER.info(() -> {
                try {
                    return "Payload " + message.getBody(String.class);
                } catch (JMSException e) {
                    LOGGER.log(Level.SEVERE, "Cannot handle message " + message, e);
                    return "";
                }
            });

    }
}
