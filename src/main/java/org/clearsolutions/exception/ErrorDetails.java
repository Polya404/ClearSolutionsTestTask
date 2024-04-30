package org.clearsolutions.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record ErrorDetails(LocalDateTime timestamp, String message, String details) {

}
