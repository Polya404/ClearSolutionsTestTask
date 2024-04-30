package org.clearsolutions.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResult {
    private boolean isRequestValid;
    private String errorCause;

    public ValidationResult(final boolean initialValidStatus) {
        this.isRequestValid = initialValidStatus;
    }
}
