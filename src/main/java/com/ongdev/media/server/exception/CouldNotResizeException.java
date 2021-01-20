package com.ongdev.media.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouldNotResizeException extends RuntimeException {
    private String message;
}
