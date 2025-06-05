package com.laboratorio.blueskyapiinterface.exception;

/**
 *
 * @author Rafael
 * @version 1.1
 * @created 01/08/2024
 * @updated 05/06/2025
 */
public class BlueskyException extends RuntimeException {
    private Throwable causaOriginal = null;
    
    public BlueskyException(String message) {
        super(message);
    }
    
    public BlueskyException(String message, Throwable causaOriginal) {
        super(message);
        this.causaOriginal = causaOriginal;
    }
    
    @Override
    public String getMessage() {
        if (this.causaOriginal != null) {
            return super.getMessage() + " | Causa original: " + this.causaOriginal.getMessage();
        }
        
        return super.getMessage();
    }
}