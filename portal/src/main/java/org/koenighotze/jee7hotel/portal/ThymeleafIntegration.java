package org.koenighotze.jee7hotel.portal;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author dschmitz
 */
@ApplicationScoped
public class ThymeleafIntegration {

    private TemplateEngine templateEngine;

    @PostConstruct
    public void init() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");
        resolver.setPrefix("/WEB-INF/templates");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Produces
    public TemplateEngine templateEngine() {
        return templateEngine;
    }
}
