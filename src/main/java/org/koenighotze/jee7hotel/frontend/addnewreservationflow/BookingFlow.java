

package org.koenighotze.jee7hotel.frontend.addnewreservationflow;

import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 *
 * @author dschmitz
 */

public class BookingFlow implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(BookingFlow.class.getName());
    
//    @Produces
//    @FlowDefinition
    public Flow defineBookingFlow(@FlowBuilderParameter FlowBuilder builder) {
        new RuntimeException().printStackTrace();
        
        LOGGER.info("Initializing flow");
        
        String flowId = "addnewreservationflow";
        builder.id("", flowId);
// 
//        flowBuilder.inboundParameter("param1FromJoinFlow", 
//                "#{flowScope.param1Value}");
//        flowBuilder.inboundParameter("param2FromJoinFlow", 
//                "#{flowScope.param2Value}");
// 
//        flowBuilder.flowCallNode("calljoin").flowReference("", "joinFlow").
//                outboundParameter("param1FromCheckoutFlow", 
//                    "#{checkoutFlowBean.name}").
//                outboundParameter("param2FromCheckoutFlow", 
//                    "#{checkoutFlowBean.city}");
        
        // define start page
        builder.viewNode(flowId, 
                "/addnewreservationflow/addnewreservationflow.xhtml")
                .markAsStartNode();
        
        
        // set the return value
        builder.returnNode("return_from_add_new_reservation_wizard")
                .fromOutcome("#{addNewReservationWizardBean.returnValue}");
        
        return builder.getFlow();
    }
}
