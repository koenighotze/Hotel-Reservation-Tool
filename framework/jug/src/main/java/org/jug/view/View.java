package org.jug.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class View implements Viewable {

    private List<String> errors;

    /** */
    private String path = null;
    private Map<String, Object> model = new HashMap<>();
    private boolean redirect = false;
    private boolean absolute = false;

    private TemplateEngine templateEngine;

    public static View of(String path, TemplateEngine templateEngine) {
        return new View(path, templateEngine);
    }

    public static View of(String path, boolean redirect) {
        return new View(path, redirect);
    }

    private View(String path, TemplateEngine templateEngine) {
        this.path = path;
        this.templateEngine = templateEngine;
    }

    private View(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public View withAbsoluteUrl() {
        this.absolute = true;
        return this;
    }

    public View withModel(String key, Object value) {
        this.model.put(key, value);
        return this;
    }
    
    public View withModel(Map<String, Object> model){
        this.model.putAll(model);
        return this;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, Object> getModel() {
        return this.model;
    }

    public String render(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, WebApplicationException {
        if (templateEngine == null) {
            throw new RuntimeException("Template Engine is null");
        }
        WebContext context = new WebContext(request, response, request.getServletContext());
        Set<Map.Entry<String, Object>> entries = this.model.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        if (errors != null && !errors.isEmpty()) {
            context.setVariable("errors", errors);
        }
        return templateEngine.process(this.path, context);
    }

    public boolean isRedirect() {
        return this.redirect;
    }

    public boolean isAbsolute() {
        return absolute;
    }
}
