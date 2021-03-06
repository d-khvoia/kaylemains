package com.cybertournaments.kaylemains.domain.service.impl;

import com.cybertournaments.kaylemains.api.TournamentManagerService;
import com.cybertournaments.kaylemains.domain.model.Match;
import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import com.cybertournaments.kaylemains.exception.IllegalParticipantAdditionException;
import com.cybertournaments.kaylemains.exception.IllegalParticipantDeletionException;
import com.cybertournaments.kaylemains.exception.IllegalParticipantsAdditionException;
import com.cybertournaments.kaylemains.repository.MatchRepository;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import com.cybertournaments.kaylemains.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.cybertournaments.kaylemains.util.NumberHelper.getNearestPowerOfTwo;
import static com.cybertournaments.kaylemains.util.NumberHelper.isPowerOfTwo;


@Service
public class TournamentManagerServiceImpl implements TournamentManagerService {

    private final ParticipantRepository participantRepository;
    private final MatchRepository matchRepository;

    public TournamentManagerServiceImpl(TournamentRepository tournamentRepository, ParticipantRepository participantRepository, MatchRepository matchRepository) {

        this.participantRepository = participantRepository;
        this.matchRepository = matchRepository;
    }

    public void addParticipant(Tournament t, String nickname) {

        Participant p = new Participant(t, nickname);

        if (!t.isStarted() && t.getParticipants().size() < t.getMaxParticipants()) {
            t.getParticipants().add(p);
            participantRepository.save(p);
        } else throw new IllegalParticipantAdditionException(p.getId());
    }

    public void addParticipant(Tournament t, Participant p) {
        if (!t.isStarted() && t.getParticipants().size() < t.getMaxParticipants()) {
            t.getParticipants().add(p);
            participantRepository.save(p);
        } else throw new IllegalParticipantAdditionException(p.getId());
    }

    public void addParticipants(Tournament t, List<Participant> participants) {
        if (!t.isStarted() && (t.getParticipants().size() + participants.size()) <= t.getMaxParticipants()) {
            t.getParticipants().addAll(participants);
            participantRepository.saveAll(participants);
        } else throw new IllegalParticipantsAdditionException();
    }

    public void deleteParticipantByIndex(Tournament t, int index) {

        Participant p = t.getParticipants().get(index);

        if (t.isOnHold()) {
            participantRepository.delete(p);
            t.getParticipants().remove(index);
        } else throw new IllegalParticipantDeletionException(p.getId());
    }

    public void deleteParticipantByID(Tournament t, Long id) {

        if (t.isOnHold()) {
            participantRepository.deleteById(id);
            t.getParticipants().removeIf(e -> e.getId() == id);
        } else throw new IllegalParticipantDeletionException(id);
    }

    public void deleteParticipant(Tournament t, Participant p) {

        if (t.isOnHold()) {
            participantRepository.delete(p);
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

        int participantsNumber = t.getParticipants().size();

        if (participantsNumber == 1) {
            return "Tournament # " + t.getId() + " is finished.\nWinner: "
                   + t.getParticipants().get(0).toString();
        } else {
            t.setCurrentRound(t.getCurrentRound() + 1);

            int firstHeldMatchIndex = t.getMatches().size();

            if (isPowerOfTwo(t.getParticipants().size())) {
                holdMatches(t,0);
            } else {
                holdIncompleteRound(t);
            }

            removeLosers(t, firstHeldMatchIndex);

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

        Match match;
        int lastMatchIndex;

        for (int i = firstToPlayIndex, j = firstToPlayIndex + 1; j < t.getParticipants().size(); i += 2, j += 2) {
            t.getMatches().add(new Match(t, t.getParticipants().get(i), t.getParticipants().get(j),
                    LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

            lastMatchIndex = t.getMatches().size() - 1;
            match = t.getMatches().get(lastMatchIndex);
            match.start();

            matchRepository.save(match);
        }
    }

    private void removeLosers(Tournament t, int firstHeldMatchIndex) {

        int lastHeldMatchIndex = t.getMatches().size() - 1;
        Match match;

        for (int i = firstHeldMatchIndex; i <= lastHeldMatchIndex; i++) {
            match = t.getMatches().get(i);
            if (match.getFirstParticipantScore() == 1) {
                participantRepository.delete(match.getSecondParticipant());
                removeParticipant(t, match.getSecondParticipant());
            } else {
                participantRepository.delete(match.getFirstParticipant());
                removeParticipant(t, match.getFirstParticipant());
            }
        }
    }

    private void removeParticipantByIndex(Tournament t, int index) {

        Participant p = t.getParticipants().get(index);
        participantRepository.delete(p);
        t.getParticipants().remove(index);
    }

    private void removeParticipantByID(Tournament t, Long id) {

        participantRepository.deleteById(id);
        t.getParticipants().removeIf(e -> e.getId() == id);
    }

    private void removeParticipant(Tournament t, Participant p) {

        participantRepository.delete(p);
        t.getParticipants().remove(p);
    }

    public ParticipantRepository getParticipantRepository() {
        return participantRepository;
    }

    public MatchRepository getMatchRepository() {
        return matchRepository;
    }
}
