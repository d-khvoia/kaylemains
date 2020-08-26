package com.cybertournaments.kaylemains.exception;

public class IllegalParticipantsAdditionException extends RuntimeException {

    public IllegalParticipantsAdditionException() {

        super("Could not add participants: either the tournament is started or participant's limit would be exceeded.");
    }
}
