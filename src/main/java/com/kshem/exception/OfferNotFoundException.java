package com.kshem.exception;

public class OfferNotFoundException extends ResourceNotFoundException {

    public OfferNotFoundException(String message) {
        super(message);
    }
}