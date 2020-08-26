package com.cybertournaments.kaylemains.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.cybertournaments.kaylemains.controller.TournamentController;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TournamentModelAssembler implements RepresentationModelAssembler<Tournament, EntityModel<Tournament>> {
    @Override
    public EntityModel<Tournament> toModel(Tournament tournament) {

        return EntityModel.of(tournament,
            linkTo(methodOn(TournamentController.class).one(tournament.getId())).withSelfRel(),
            linkTo(methodOn(TournamentController.class).all()).withRel("tournaments"));
    }
}
