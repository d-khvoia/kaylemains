package com.cybertournaments.kaylemains.controller;

import com.cybertournaments.kaylemains.controller.assembler.ParticipantModelAssembler;
import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.exception.ParticipantNotFoundException;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ParticipantController {

    private final ParticipantRepository repository;

    private final ParticipantModelAssembler assembler;

    public ParticipantController(ParticipantRepository repository, ParticipantModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/participants/{id}")
    public EntityModel<Participant> one(@PathVariable Long id) {

        Participant participant = repository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(id));

        return assembler.toModel(participant);
    }

    @GetMapping("/participants")
    public CollectionModel<EntityModel<Participant>> all() {

        List<EntityModel<Participant>> participants = repository.findAll().stream()
                .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(participants, linkTo(methodOn(ParticipantController.class).all()).withSelfRel());
    }
}
