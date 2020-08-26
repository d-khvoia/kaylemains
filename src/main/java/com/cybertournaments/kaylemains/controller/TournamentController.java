package com.cybertournaments.kaylemains.controller;

import com.cybertournaments.kaylemains.api.TournamentManagerService;
import com.cybertournaments.kaylemains.controller.assembler.ParticipantModelAssembler;
import com.cybertournaments.kaylemains.controller.assembler.TournamentModelAssembler;
import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import com.cybertournaments.kaylemains.exception.TournamentNotFoundException;
import com.cybertournaments.kaylemains.repository.TournamentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TournamentController {

    private final TournamentManagerService tournamentManagerService;

    private final TournamentRepository repository;

    private final TournamentModelAssembler tournamentModelAssembler;
    private final ParticipantModelAssembler participantModelAssembler;

    public TournamentController(TournamentManagerService tms, TournamentRepository rep, TournamentModelAssembler tma, ParticipantModelAssembler pma) {

        tournamentManagerService = tms;
        repository = rep;
        tournamentModelAssembler = tma;
        participantModelAssembler = pma;
    }

    @GetMapping("/tournaments/{id}")
    public EntityModel<Tournament> one(@PathVariable Long id) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        return tournamentModelAssembler.toModel(tournament);
    }

    @GetMapping("/tournaments")
    public CollectionModel<EntityModel<Tournament>> all() {

        List<EntityModel<Tournament>> tournaments = repository.findAll().stream()
                .map(tournamentModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(tournaments, linkTo(methodOn(TournamentController.class).all()).withSelfRel());
    }

    @GetMapping("/tournaments/{id}/participants")
    public CollectionModel<EntityModel<Participant>> allParticipants(@PathVariable Long id) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        List<EntityModel<Participant>> participants = tournament.getParticipants().stream()
                .map(participantModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(participants, linkTo(methodOn(TournamentController.class).all()).withSelfRel());
    }

    @PostMapping("/tournaments")
    public Tournament newTournament(@RequestBody Tournament newTournament) {
        return repository.save(newTournament);
    }

    @PutMapping("/tournaments/{id}")
    public List<Participant> addParticipants(@PathVariable Long id, @RequestBody List<Participant> participants) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.addParticipants(tournament, participants);

        return participants;
    }

    @PutMapping("/tournaments/{id}")
    public Participant addParticipant(@PathVariable Long id, @RequestBody Participant participant) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.addParticipant(tournament, participant);

        return participant;
    }

    @PutMapping("/tournaments/{id}")
    public Tournament startTournament(@PathVariable Long id) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.start(tournament);

        return repository.save(tournament);
    }

    @PutMapping("/tournaments/{id}")
    public Tournament pauseTournament(@PathVariable Long id) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.pause(tournament);

        return repository.save(tournament);
    }

    @PutMapping("/tournaments/{id}")
    public Tournament resumeTournament(@PathVariable Long id) {

        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.resume(tournament);

        return repository.save(tournament);
    }

    @PutMapping("/tournaments/{id}")
    public Tournament holdRound(@PathVariable Long id) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new TournamentNotFoundException(id));

        tournamentManagerService.holdRound(tournament);

        return repository.save(tournament);
    }

    @DeleteMapping("/tournaments/{tournamentID}/participants/{participantID}")
    public void deleteParticipant(@PathVariable Long tournamentID, @PathVariable Long participantID) {
        Tournament tournament = repository.findById(tournamentID)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentID));

        tournamentManagerService.removeParticipantByID(tournament, participantID);
    }

    @DeleteMapping("/tournaments/{id}")
    public void deleteTournament(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
