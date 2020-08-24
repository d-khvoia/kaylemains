package com.cybertournaments.kaylemains.config;

import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import com.cybertournaments.kaylemains.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TournamentRepository tournamentRepository, ParticipantRepository participantRepository) {

        return args -> {
            Tournament kayleMains = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

            log.info("Preloading " + tournamentRepository.save(kayleMains));

            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"Dixon")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"Turkis")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"ArchMichaelWeb")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain1")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain2")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain3")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain4")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain5")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain6")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain7")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain8")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain9")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain10")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain11")));
            log.info("Preloading " + participantRepository.save(new Participant(kayleMains,"RandomKayleMain12")));
        };
    }
}
