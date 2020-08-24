package com.cybertournaments.kaylemains.repository;

import com.cybertournaments.kaylemains.domain.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
