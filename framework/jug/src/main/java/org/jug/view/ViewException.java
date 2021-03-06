package org.jug.view;

import java.io.Serializable;

import org.thymeleaf.TemplateEngine;

/**
 * Created by shekhargulati on 21/03/14.
 */
public class ViewException extends RuntimeException implements Serializable {

	private TemplateEngine templateEngine;
	private String message;

	public ViewException(String message, TemplateEngine templateEngine) {
		super(message);
		this.message = message;
		this.templateEngine = templateEngine;
	}

	public ViewException(String message, Throwable cause, TemplateEngine templateEngine) {
		super(message, cause);
		this.message = message;
		this.templateEngine = templateEngine;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}
	
}
