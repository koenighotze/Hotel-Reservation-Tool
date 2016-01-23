package org.jug;

import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.HttpServletResponseWrapper;
import org.jboss.resteasy.spi.HttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class JugFilterDispatcher extends FilterDispatcher {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public HttpResponse createResteasyHttpResponse(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response, getDispatcher().getProviderFactory()) {

            protected OutputStream getSuperOuptutStream() throws IOException {
                return super.getOutputStream();
            }

            public OutputStream getOutputStream() throws IOException {
                return new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        getSuperOuptutStream().write(b);
                    }

                    @Override
                    public void write(byte[] b) throws IOException {
                        getSuperOuptutStream().write(b);
                    }

                    @Override
                    public void write(byte[] b, int off, int len)
                            throws IOException {
                        getSuperOuptutStream().write(b, off, len);
                    }

                    @Override
                    public void flush() throws IOException {
                        getSuperOuptutStream().flush();
                    }

                    @Override
                    public void close() throws IOException {
                        getSuperOuptutStream().close();
                    }
                };
            }


        };
    }
}
