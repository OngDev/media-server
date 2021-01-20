package com.ongdev.media.server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityUpdateFailedException extends RuntimeException{
    private String message;
}
