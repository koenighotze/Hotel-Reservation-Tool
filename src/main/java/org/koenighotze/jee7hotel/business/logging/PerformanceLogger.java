package org.koenighotze.jee7hotel.business.logging;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Created by dschmitz on 14.11.14.
 */

@Interceptor
@PerformanceLog
public class PerformanceLogger {
    private static final Logger LOGGER = Logger.getLogger(PerformanceLogger.class.getName());

    @AroundInvoke
    public Object around(InvocationContext ctx) throws Exception {
        // obviously not a good way to log stuff...
        // should demonstrate context, uuid, service domain, etc.

        long start = System.currentTimeMillis();

        // using a supplier defers eval of string
        LOGGER.fine("Entering " + ctx.getMethod().getName());

        try {
            return ctx.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            LOGGER.fine("Leaving " + ctx.getMethod().getName() + "; Took " + duration + " ms");
        }

    }
}
