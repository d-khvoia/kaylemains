package com.cybertournaments.kaylemains.controller;

import com.cybertournaments.kaylemains.domain.model.Match;
import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import com.cybertournaments.kaylemains.exception.MatchNotFoundException;
import com.cybertournaments.kaylemains.exception.ParticipantNotFoundException;
import com.cybertournaments.kaylemains.exception.TournamentNotFoundException;
import com.cybertournaments.kaylemains.repository.MatchRepository;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import com.cybertournaments.kaylemains.repository.TournamentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static com.cybertournaments.kaylemains.api.TournamentManagerService.*;

@RestController
public class TournamentController {
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final ParticipantRepository participantRepository;

    public TournamentController(TournamentRepository tournamentRepository, ParticipantRepository participantRepository, MatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/tournaments")
    CollectionModel<EntityModel<Tournament>> all() {

        List<EntityModel<Tournament>> tournaments = tournamentRepository.findAll().stream()
                .map(tournament -> EntityModel.of(tournament,
                        linkTo(methodOn(TournamentController.class).one(tournament.getId())).withSelfRel(),
                        linkTo(methodOn(TournamentController.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(tournaments, linkTo(methodOn(TournamentController.class).all()).withSelfRel());
    }

    @PostMapping("/tournaments")
    public Tournament newTournament(@RequestBody Tournament newTournament) {
        return tournamentRepository.save(newTournament);
    }

    @GetMapping("/tournaments/{id}/addParticipants")
    public void addParticipantsToTournament(@PathVariable Long id, @RequestBody List<Participant> participants) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
        addParticipants(tournament, participants);
        participantRepository.saveAll(participants);
    }

    @GetMapping("matches/{id}/summary")
    public String getMatchSummary(@PathVariable Long id) {
        Match m = matchRepository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));
        return m.getSummary();
    }

    @DeleteMapping("/tournaments/{id}/participants/{pid}")
    public void deleteParticipant(@PathVariable Long id, @PathVariable Long pid) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(pid));
        removeParticipant(tournament, participant);
        participantRepository.delete(participant);
    }

    @GetMapping("/tournaments/{id}/run")
    public void runTournament(@PathVariable Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
        while (tournament.getParticipants().size() != 1)
            holdRound(tournament);
    }

    @GetMapping("/tournaments/{id}/start")
    public void startTournament(@PathVariable Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));
        start(tournament);
    }

    @GetMapping("/tournaments/{id}")
    public EntityModel<Tournament> one(@PathVariable Long id) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new TournamentNotFoundException(id));

        return EntityModel.of(tournament,
                linkTo(methodOn(TournamentController.class).one(id)).withSelfRel(),
                linkTo(methodOn(TournamentController.class).all()).withRel("tournaments"));
    }

    @DeleteMapping("/tournaments/{id}")
    public void deleteTournament(@PathVariable Long id) {
        tournamentRepository.deleteById(id);
    }
}
