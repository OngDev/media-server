package com.ongdev.media.server.controller.exception;

public class FileException extends RuntimeException {

	public FileException(String message) {
        super(message);
    }

	public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}

