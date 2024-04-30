package org.clearsolutions.exception;

import static java.lang.String.format;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(final String message, final Object o) {
        super(format(message, o));
    }

    public InvalidDataException(final String message) {
        super(message);
    }
}
