package main.service;

import java.util.Map;

public class BadRequestException extends RuntimeException {
	private Map<String, String> errors;

	public BadRequestException(Map<String, String> errors) {
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}
}
