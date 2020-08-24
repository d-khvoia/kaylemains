package com.cybertournaments.kaylemains.exception;

public class ParticipantNotFoundException extends RuntimeException {

    public ParticipantNotFoundException(Long id) {
        super("Could not find participant " + id);
    }
}
