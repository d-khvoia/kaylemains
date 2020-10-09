package com.cybertournaments.kaylemains.controller;

import com.cybertournaments.kaylemains.controller.assembler.MatchModelAssembler;
import com.cybertournaments.kaylemains.domain.model.Match;
import com.cybertournaments.kaylemains.exception.MatchNotFoundException;
import com.cybertournaments.kaylemains.repository.MatchRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MatchController {

    private final MatchRepository repository;

    private final MatchModelAssembler assembler;

    public MatchController(MatchRepository repository, MatchModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/matches/{id}")
    public EntityModel<Match> one(@PathVariable Long id) {

        Match match = repository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        return assembler.toModel(match);
    }

    @GetMapping("/matches")
    public CollectionModel<EntityModel<Match>> all() {

        List<EntityModel<Match>> matches = repository.findAll().stream()
            .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(matches, linkTo(methodOn(MatchController.class).all()).withSelfRel());
    }

    @GetMapping("matches/{id}")
    public String summary(@PathVariable Long id) {

        Match m = repository.findById(id)
                .orElseThrow(() -> new MatchNotFoundException(id));

        return m.getSummary();
    }
}
