package com.cybertournaments.kaylemains.controller.assembler;

import com.cybertournaments.kaylemains.controller.MatchController;
import com.cybertournaments.kaylemains.domain.model.Match;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MatchModelAssembler implements RepresentationModelAssembler<Match, EntityModel<Match>> {
    @Override
    public EntityModel<Match> toModel(Match match) {

        return EntityModel.of(match,
            linkTo(methodOn(MatchController.class).one(match.getId())).withSelfRel(),
            linkTo(methodOn(MatchController.class).all()).withRel("matches"));
    }
}
