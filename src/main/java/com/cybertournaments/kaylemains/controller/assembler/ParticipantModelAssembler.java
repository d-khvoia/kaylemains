package com.cybertournaments.kaylemains.controller.assembler;

import com.cybertournaments.kaylemains.controller.ParticipantController;
import com.cybertournaments.kaylemains.domain.model.Participant;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParticipantModelAssembler implements RepresentationModelAssembler<Participant, EntityModel<Participant>> {
    @Override
    public EntityModel<Participant> toModel(Participant participant) {

        return EntityModel.of(participant,
            linkTo(methodOn(ParticipantController.class).one(participant.getId())).withSelfRel(),
            linkTo(methodOn(ParticipantController.class).all()).withRel("participants"));
    }
}
