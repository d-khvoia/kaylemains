package com.cybertournaments.kaylemains.api;

import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import com.cybertournaments.kaylemains.repository.MatchRepository;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TournamentManagerService {

    void addParticipant(Tournament t, String nickname);
    void addParticipant(Tournament t, Participant p);
    void addParticipants(Tournament t, List<Participant> participants);
    void removeParticipantByIndex(Tournament t, int index);
    void removeParticipantByID(Tournament t, Long id);
    void removeParticipant(Tournament t, Participant p);
    void start(Tournament t);
    void pause(Tournament t);
    void resume(Tournament t);
    String holdRound(Tournament t);

    public ParticipantRepository getParticipantRepository();
    public MatchRepository getMatchRepository();
}
