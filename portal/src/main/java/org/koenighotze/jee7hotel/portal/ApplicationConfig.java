package org.koenighotze.jee7hotel.portal;

import org.jug.view.NotFoundExceptionMapper;
import org.jug.view.ViewExceptionMapper;
import org.jug.view.ViewResourceNotFoundExceptionMapper;
import org.jug.view.ViewWriter;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dschmitz
 */
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(NotFoundExceptionMapper.class);
        classes.add(ViewWriter.class);
        classes.add(ViewExceptionMapper.class);
        classes.add(ViewResourceNotFoundExceptionMapper.class);


        classes.add(IndexView.class);
        return classes;
    }
}
