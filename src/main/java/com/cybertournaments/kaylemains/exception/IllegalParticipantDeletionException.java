package com.cybertournaments.kaylemains.exception;

public class IllegalParticipantDeletionException extends RuntimeException {

    public IllegalParticipantDeletionException(Long id) {

        super("Could not remove participant " + id
              + ": the tournament is currently in progress.");
    }
}
