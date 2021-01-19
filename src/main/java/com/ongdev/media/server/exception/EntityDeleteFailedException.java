package com.ongdev.media.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDeleteFailedException extends RuntimeException {
    private String message;
}

