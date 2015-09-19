package org.koenighotze.jee7hotel.frontend;

import javax.enterprise.inject.Model;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author koenighotze
 */
@Model
public class ErrorBean {
    private static final Logger LOGGER = Logger.getLogger(ErrorBean.class.getName());

    public String getExceptionStackTrace(Throwable t) {
        if (null == t) {
            return "";
        }

        LOGGER.log(Level.SEVERE, "Unexpected error condition", t);
        StringWriter sw = new StringWriter();
        try (PrintWriter w = new PrintWriter(sw)) {
            t.printStackTrace(w);
        }
        return sw.toString();
    }
}
