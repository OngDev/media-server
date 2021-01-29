package com.ongdev.media.server.exception.controller;

import com.ongdev.media.server.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(EntityNotFoundException ex) {
        String customEx = "Could not find\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityCreateFailedException.class)
    public ResponseEntity<Object> handleCreateFailedException(EntityCreateFailedException ex) {
        String customEx = "Could not save\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityDeleteFailedException.class)
    public ResponseEntity<Object> handleDeleteFailedException(EntityDeleteFailedException ex) {
        String customEx = "Could not delete\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityUpdateFailedException.class)
    public ResponseEntity<Object> handleUpdateFailedException(EntityUpdateFailedException ex) {
        String customEx = "Could not update\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CouldNotResizeException.class)
    public ResponseEntity<Object> handleCouldNotResizeException(CouldNotResizeException ex) {
        String customEx = "Could not resize\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CouldNotShowImage.class)
    public ResponseEntity<Object> handleCouldNotShowImageException(CouldNotShowImage ex) {
        String customEx = "Could not show image\n " + ex.getMessage();
        return new ResponseEntity<>(customEx, HttpStatus.BAD_REQUEST);
    }

}
