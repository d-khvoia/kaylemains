package com.cybertournaments.kaylemains.exception;

public class TournamentNotFoundException extends RuntimeException {

    public TournamentNotFoundException(Long id) {
        super("Could not find tournament " + id);
    }
}
