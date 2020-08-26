package com.cybertournaments.kaylemains.exception;

public class IllegalParticipantAdditionException extends RuntimeException {

    public IllegalParticipantAdditionException(Long id) {

        super("Could not add participant " + id
              + ": either the tournament is started or participant's limit would be exceeded.");
    }
}
