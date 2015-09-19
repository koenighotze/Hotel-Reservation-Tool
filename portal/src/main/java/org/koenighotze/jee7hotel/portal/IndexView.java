package org.koenighotze.jee7hotel.portal;

import org.jug.view.View;
import org.thymeleaf.TemplateEngine;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author dschmitz
 */
@Path("/")
public class IndexView {
    @Inject
    private TemplateEngine templateEngine;

    @GET
    public View index() {
        return View.of("/index", templateEngine);
    }
}
