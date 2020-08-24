package com.cybertournaments.kaylemains.api;

import com.cybertournaments.kaylemains.domain.model.Match;
import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.cybertournaments.kaylemains.util.NumberHelper.getNearestPowerOfTwo;
import static com.cybertournaments.kaylemains.util.NumberHelper.isPowerOfTwo;

public interface TournamentManagerService {
    static void addParticipant(Tournament t, String nickname) {
        if (!t.isStarted() && t.getParticipants().size() < t.getMaxParticipants())
            t.getParticipants().add(new Participant(t, nickname));
        else throw new IllegalStateException();
    }

    static void addParticipant(Tournament t, Participant p) {
        if (!t.isStarted() && t.getParticipants().size() < t.getMaxParticipants())
            t.getParticipants().add(p);
        else throw new IllegalStateException();
    }

    static void addParticipants(Tournament t, List<Participant> participants) {
        if (!t.isStarted() && (t.getParticipants().size() + participants.size()) < t.getMaxParticipants())
            t.getParticipants().addAll(participants);
    }

    static void removeParticipantByIndex(Tournament t, int index) {
        if (t.isOnHold())
            t.getParticipants().remove(index);
        else throw new IllegalStateException();
    }
    static void removeParticipantByID(Tournament t, Long id) {
        if (t.isOnHold())
            t.getParticipants().removeIf(e -> e.getId() == id);
        else throw new IllegalStateException();
    }

    static void removeParticipant(Tournament t, Participant p) {
        if (t.isOnHold())
            t.getParticipants().remove(p);
        else throw new IllegalStateException();
    }

    static void start(Tournament t) {
        t.setStarted(true);
        t.setNumberOfMatches(t.getParticipants().size() - 1);

        //Randomizing participants
        Collections.shuffle(t.getParticipants());
    }

    static void pause(Tournament t) { t.setOnHold(true); }

    static void unPause(Tournament t) { t.setOnHold(false); }

    static void holdRound(Tournament t) {
        t.setCurrentRound(t.getCurrentRound() + 1);
        int[] heldMatchesIndices;

        if (isPowerOfTwo(t.getParticipants().size())) {
            heldMatchesIndices = holdMatches(t,0);
        } else {
            heldMatchesIndices = holdIncompleteRound(t);
        }

        removeLosers(t, heldMatchesIndices[0], heldMatchesIndices[1]);
    }

    private static int[] holdIncompleteRound(Tournament t) {
        int nearestPowerOfTwo = getNearestPowerOfTwo(t.getParticipants().size());

        //I didn't come up with this formula.
        // It was given to me by a person who organizes single-elimination Heroes III tournaments.
        int byeQuantity = 2 * nearestPowerOfTwo - t.getParticipants().size();

        //Randomizing participants for random players to get bye
        Collections.shuffle(t.getParticipants());

        //Participants with ArrayList indices from 0 to byeQuantity - 1 don't play and pass to the next round automatically.
        return holdMatches(t, byeQuantity);
    }

    //Pairing participants, creating and starting matches
    private static int[] holdMatches(Tournament t, int firstToPlayIndex) {
        int[] heldMatchesIndices = new int[2];
        heldMatchesIndices[0] = heldMatchesIndices[1] = t.getMatches().size();

        for (int i = firstToPlayIndex, j = firstToPlayIndex + 1; j < t.getParticipants().size(); i += 2, j += 2) {
            t.getMatches().add(new Match(t, t.getParticipants().get(i), t.getParticipants().get(j),
                    LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
            t.getMatches().get(heldMatchesIndices[1]++).start();
        }

        return heldMatchesIndices;
    }

    private static void removeLosers(Tournament t, int firstHeldMatchIndex, int lastHeldMatchIndex) {
        Match m;

        for (int i = firstHeldMatchIndex; i < lastHeldMatchIndex; i++) {
            m = t.getMatches().get(i);
            if (m.getFirstParticipantScore() == 1) {
                removeParticipant(t, m.getSecondParticipant());
            } else {
                removeParticipant(t, m.getFirstParticipant());
            }
        }
    }
}
