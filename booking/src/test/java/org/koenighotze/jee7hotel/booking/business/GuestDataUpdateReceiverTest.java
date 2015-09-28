package org.koenighotze.jee7hotel.booking.business;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.koenighotze.jee7hotel.booking.domain.GuestModel;
import org.koenighotze.jee7hotel.booking.persistence.GuestModelRepository;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

/**
 * @author dschmitz
 */
public class GuestDataUpdateReceiverTest {

    @Mocked
    private Message textMessage;

    @Mocked
    private GuestModelRepository guestModelRepository;

    @Test
    public void a_valid_text_message_gets_processed() throws JMSException, IOException {
        GuestDataUpdateReceiver receiver = new GuestDataUpdateReceiver(guestModelRepository);

        final String messagePayload = readValidGuestXml();

        new Expectations() {{
            textMessage.getBody(String.class);
            result = messagePayload;
        }};

        receiver.onMessage(textMessage);
    }

    protected String readValidGuestXml() throws IOException {
        final String messagePayload;
        try (final BufferedReader br
                     = new BufferedReader(
                new InputStreamReader(GuestDataUpdateReceiverTest.class.getResourceAsStream("/org/koenighotze/jee7hotel/booking/domain/validguest.xml"), Charset.forName("UTF-8")))) {
            messagePayload = br.lines().collect(joining(lineSeparator()));
        }
        return messagePayload;
    }

    @Test
    public void valid_guest_models_are_stored() throws JMSException, IOException {
        GuestDataUpdateReceiver receiver = new GuestDataUpdateReceiver(guestModelRepository);

        final String messagePayload = readValidGuestXml();

        new Expectations() {{
            textMessage.getBody(String.class);
            result = messagePayload;
        }};

        receiver.onMessage(textMessage);

        new Verifications() {{
            guestModelRepository.storeGuestModel(withInstanceOf(GuestModel.class));
            times = 1;
        }};
    }

    @Test
    public void invalid_messages_are_not_stored() throws JMSException {
        GuestDataUpdateReceiver receiver = new GuestDataUpdateReceiver(guestModelRepository);

        final String messagePayload = "CRAP";

        new Expectations() {{
            textMessage.getBody(String.class);
            result = messagePayload;
        }};

        receiver.onMessage(textMessage);

        new Verifications() {{
            guestModelRepository.storeGuestModel(withInstanceOf(GuestModel.class));
            times = 0;
        }};
    }
}