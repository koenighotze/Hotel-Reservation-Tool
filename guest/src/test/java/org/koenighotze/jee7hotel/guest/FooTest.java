package org.koenighotze.jee7hotel.guest;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author dschmitz
 */
public class FooTest {

    @Test
    public void getid() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        assertThat(localHost.getHostName()).isEqualTo("schda-ambp2.local");


    }
}
