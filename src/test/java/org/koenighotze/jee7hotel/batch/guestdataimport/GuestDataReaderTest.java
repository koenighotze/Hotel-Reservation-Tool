package org.koenighotze.jee7hotel.batch.guestdataimport;

import org.junit.Test;

import java.io.BufferedReader;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GuestDataReaderTest {
    @Test
    @org.junit.Ignore
    public void testReadValidObject() throws Exception {
        GuestDataReader guestDataReader = new GuestDataReader();

        BufferedReader mockReader =  mock(BufferedReader.class);
        when(mockReader.readLine()).thenReturn("Commander Schnitzenbirtz, foo@bar.com");
        guestDataReader.setReader(mockReader);

        Object item = guestDataReader.readItem();
        assertThat(item, is(instanceOf(String.class)));

        assertThat("Commander Schnitzenbirtz, foo@bar.com", is(equalTo(item)));
    }
}
