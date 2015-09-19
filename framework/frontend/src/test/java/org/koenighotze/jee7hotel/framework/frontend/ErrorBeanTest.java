package org.koenighotze.jee7hotel.framework.frontend;

import org.junit.Test;
import org.koenighotze.jee7hotel.frontend.ErrorBean;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class ErrorBeanTest {

    @Test
    public void testGetExceptionStackTrace() throws Exception {
        RuntimeException exception = new RuntimeException("foo");
        exception.initCause(new RuntimeException(new IOException("BOOM")));

        String stackTrace = new ErrorBean().getExceptionStackTrace(exception);
        assertTrue(Stream.of(stackTrace)
                .filter(s -> s.contains("BOOM"))
                .findAny()
                .isPresent());
        assertTrue(stackTrace.contains("foo"));
    }
}