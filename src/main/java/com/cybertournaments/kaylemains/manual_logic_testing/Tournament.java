package com.cybertournaments.kaylemains.manual_logic_testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cybertournaments.kaylemains.util.NumberHelper.isPowerOfTwo;

public class Tournament {

    private Long id;
    private String name;

    private boolean isStarted = false;
    private boolean isOnHold = false;
    private boolean isFinished = false;

    private int maxParticipants;

    //Depends on the actual number of participants, N, at tournament start.
    // Total number of matches is always equal to N - 1 regardless of whether N is a power of 2 or not.
    private int numberOfMatches;

    private int currentRound = 0;

    private List<Match> matches = new ArrayList<Match>();

    private List<Participant> participants = new ArrayList<Participant>();

    public Tournament(Long id, String name) {
        this.name = name;
        maxParticipants = 8;
    }

    public Tournament(Long id, String name, int maxParticipants) {
        if (maxParticipants >= 8 && isPowerOfTwo(maxParticipants)) {
            this.id = id;
            this.maxParticipants = maxParticipants;
            this.name = name;
        } else throw new IllegalArgumentException();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isOnHold() {
        return isOnHold;
    }

    public void setOnHold(boolean onHold) {
        isOnHold = onHold;
    }

    public boolean isFinished() { return isFinished; }

    public void setFinished(boolean finished) { isFinished = finished; }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tournament))
            return false;
        Tournament t = (Tournament) o;
        return Objects.equals(id, t.id) && Objects.equals(isStarted, t.isStarted)
               && Objects.equals(name, t.name) && Objects.equals(isOnHold, t.isOnHold)
               && Objects.equals(maxParticipants, t.maxParticipants)
               && Objects.equals(numberOfMatches, t.numberOfMatches)
               && Objects.equals(currentRound, t.currentRound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isStarted, isOnHold, maxParticipants, numberOfMatches, currentRound);
    }

    @Override
    public String toString() {
        if (isFinished) {
            return "Tournament # " + id + " | Name: " + name + " is finished.\nWinner: "
                    + participants.get(0).toString();
        } else {
            String result = "Tournament # " + id + " | Name: " + name
                    + "\nMaximum number of participants: " + maxParticipants
                    + " | Current number of participants: " + participants.size()
                    + "\nNumber of single-elimination matches to be played: " + numberOfMatches
                    + " | Number of current matches: " + matches.size()
                    + "\nCurrent round: " + currentRound;

            if (isStarted) {
                result += "\nThe tournament has started";
                if (isOnHold) {
                    result += " | The tournament is currently on hold";
                } else {
                    result += " | The tournament is currently in progress";
                }
            } else {
                result += "\nThe tournament has not been started yet";
            }
            return result;
        }
    }
}
