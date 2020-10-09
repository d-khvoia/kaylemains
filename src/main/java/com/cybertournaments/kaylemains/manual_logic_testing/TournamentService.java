package com.cybertournaments.kaylemains.manual_logic_testing;

import com.cybertournaments.kaylemains.exception.IllegalParticipantAdditionException;
import com.cybertournaments.kaylemains.exception.IllegalParticipantDeletionException;
import com.cybertournaments.kaylemains.exception.IllegalParticipantsAdditionException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.cybertournaments.kaylemains.util.NumberHelper.getNearestPowerOfTwo;
import static com.cybertournaments.kaylemains.util.NumberHelper.isPowerOfTwo;

public class TournamentService {

    public void addParticipant(Long participantID, Tournament tournament, String nickname) {

        Participant p = new Participant(participantID, tournament, nickname);

        if (!tournament.isStarted() && tournament.getParticipants().size() < tournament.getMaxParticipants()) {
            tournament.getParticipants().add(p);
        } else throw new IllegalParticipantAdditionException(p.getId());
    }

    public void addParticipant(Tournament t, Participant p) {
        if (!t.isStarted() && t.getParticipants().size() < t.getMaxParticipants()) {
            t.getParticipants().add(p);
        } else throw new IllegalParticipantAdditionException(p.getId());
    }

    public void addParticipants(Tournament t, List<Participant> participants) {
        if (!t.isStarted() && (t.getParticipants().size() + participants.size()) <= t.getMaxParticipants()) {
            t.getParticipants().addAll(participants);
        } else throw new IllegalParticipantsAdditionException();
    }

    public void deleteParticipantByIndex(Tournament t, int index) {

        Participant p = t.getParticipants().get(index);

        if (t.isOnHold()) {
            t.getParticipants().remove(index);
        } else throw new IllegalParticipantDeletionException(p.getId());
    }

    public void deleteParticipantByID(Tournament t, Long participantID) {

        if (t.isOnHold()) {
            t.getParticipants().removeIf(e -> e.getId() == participantID);
        } else throw new IllegalParticipantDeletionException(participantID);
    }

    public void deleteParticipant(Tournament t, Participant p) {

        if (t.isOnHold()) {
            t.getParticipants().remove(p);
        } else throw new IllegalParticipantDeletionException(p.getId());
    }

    public void start(Tournament t) {

        t.setStarted(true);
        t.setNumberOfMatches(t.getParticipants().size() - 1);

        //Randomizing participants
        Collections.shuffle(t.getParticipants());
    }

    public void pause(Tournament t) { t.setOnHold(true); }

    public void resume(Tournament t) { t.setOnHold(false); }

    public String holdRound(Tournament t) {

        if (t.isFinished()) {
            return t.toString();
        } else {
            t.setCurrentRound(t.getCurrentRound() + 1);

            int firstHeldMatchIndex = t.getMatches().size();

            if (isPowerOfTwo(t.getParticipants().size())) {
                holdMatches(t,0);
            } else {
                holdIncompleteRound(t);
            }

            removeLosers(t, firstHeldMatchIndex);

            if (t.getParticipants().size() == 1) {
                t.setFinished(true);
            }

            return "Tournament # " + t.getId() + " | Round # " + t.getCurrentRound() + " completed.";
        }
    }

    private void holdIncompleteRound(Tournament t) {

        int nearestPowerOfTwo = getNearestPowerOfTwo(t.getParticipants().size());

        //I didn't come up with this formula.
        // It was given to me by a person who organizes single-elimination Heroes III tournaments.
        int byeQuantity = 2 * nearestPowerOfTwo - t.getParticipants().size();

        //Randomizing participants for random players to get bye
        Collections.shuffle(t.getParticipants());

        //Participants with ArrayList indices from 0 to byeQuantity - 1 don't play and pass to the next round automatically.
        holdMatches(t, byeQuantity);
    }

    //Pairing participants, creating and starting matches
    private void holdMatches(Tournament t, int firstToPlayIndex) {

        int lastMatchIndex = t.getMatches().size() - 1;
        long newMatchID;

        if (lastMatchIndex == -1) {
            newMatchID = 1;
        } else {
            newMatchID = t.getMatches().get(lastMatchIndex).getId() + 1;
        }

        Match match;

        for (int i = firstToPlayIndex, j = firstToPlayIndex + 1; j < t.getParticipants().size(); i += 2, j += 2) {
            match = new Match(newMatchID++, t, t.getParticipants().get(i), t.getParticipants().get(j),
                    LocalDateTime.now(), LocalDateTime.now().plusDays(1));
            t.getMatches().add(match);

            match.start();
        }
    }

    private void removeLosers(Tournament t, int firstHeldMatchIndex) {

        int lastHeldMatchIndex = t.getMatches().size() - 1;
        Match match;

        for (int i = firstHeldMatchIndex; i <= lastHeldMatchIndex; i++) {
            match = t.getMatches().get(i);
            if (match.getFirstParticipantScore() == 1) {
                removeParticipant(t, match.getSecondParticipant());
            } else {
                removeParticipant(t, match.getFirstParticipant());
            }
        }
    }

    private void removeParticipantByIndex(Tournament t, int index) {

        Participant p = t.getParticipants().get(index);
        t.getParticipants().remove(index);
    }

    private void removeParticipantByID(Tournament t, Long id) {

        t.getParticipants().removeIf(e -> e.getId() == id);
    }

    private void removeParticipant(Tournament t, Participant p) {

        t.getParticipants().remove(p);
    }

}
