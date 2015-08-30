

package org.koenighotze.jee7hotel.booking.frontend.addnewreservationflow;

import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Example definition of a Java-Style flow.
 * @author dschmitz
 */
public class BookingFlow implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(BookingFlow.class.getName());

    // Deactivated as the flow is defined in XML
//    @Produces
//    @FlowDefinition
    public Flow defineBookingFlow(@FlowBuilderParameter FlowBuilder builder) {
        LOGGER.info("Initializing flow for reservation creation");

        String flowId = "addnewreservationflow";
        builder.id("", flowId);

        // define start page
        builder.viewNode(flowId,
                "/addnewreservationflow/addnewreservationflow.xhtml")
                .markAsStartNode();

        // set the return value
        builder.returnNode("return_from_add_new_reservation_wizard")
                .fromOutcome("#{addNewReservationWizardBean.confirmBooking()}");

        return builder.getFlow();
    }
}
