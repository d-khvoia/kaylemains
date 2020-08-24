package com.cybertournaments.kaylemains.repository;

import com.cybertournaments.kaylemains.domain.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
