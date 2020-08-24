package com.cybertournaments.kaylemains.repository;

import com.cybertournaments.kaylemains.domain.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
