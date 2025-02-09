package com.api.sgri.exception;

public class DuplicateRequerimientoException extends Exception {
    public DuplicateRequerimientoException() {
        super();
    }

    public DuplicateRequerimientoException(String message) {
        super(message);
    }

    public DuplicateRequerimientoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateRequerimientoException(Throwable cause) {
        super(cause);
    }
}