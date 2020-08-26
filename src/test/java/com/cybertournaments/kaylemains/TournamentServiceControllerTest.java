package com.cybertournaments.kaylemains;

import com.cybertournaments.kaylemains.api.TournamentManagerService;
import com.cybertournaments.kaylemains.domain.service.impl.TournamentManagerServiceImpl;
import com.cybertournaments.kaylemains.repository.MatchRepository;
import com.cybertournaments.kaylemains.repository.ParticipantRepository;
import com.cybertournaments.kaylemains.repository.TournamentRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cybertournaments.kaylemains.domain.model.Participant;
import com.cybertournaments.kaylemains.domain.model.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class TournamentServiceControllerTest extends AbstractTest {

    private final TournamentManagerService tournamentManagerService;

    public TournamentServiceControllerTest(TournamentManagerService tournamentManagerService) {
        this.tournamentManagerService = tournamentManagerService;
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void one() throws Exception {
        String uri = "/tournaments/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Tournament tournament = super.mapFromJson(content, Tournament.class);
        assertTrue(tournament.getName().equals("Kayle Mains Competition: Summoner's Gorge"));
    }

    @Test
    public void all() throws Exception {
        String uri = "/tournaments";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Tournament[] tournamentList = super.mapFromJson(content, Tournament[].class);
        assertTrue(tournamentList.length > 0);
    }

    @Test
    public void allParticipants() throws Exception {
        String uri = "/tournaments/1";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Participant[] participantList = super.mapFromJson(content, Participant[].class);
        assertTrue(participantList.length == 15);
    }

    @Test
    public void newTournament() throws Exception {
        String uri = "/tournaments";
        Tournament tournament = new Tournament();
        tournament.setId(2L);
        tournament.setName("Kayle Mains Competition: Howling Abyss");

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Tournament is created successfully");
    }

    @Test
    public void addParticipant() throws Exception {
        String uri = "/tournaments/1";
        Participant participant = new Participant();
        participant.setNickname("Random25");

        String inputJson = super.mapToJson(participant);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Participant is added successfully");
    }

    @Test
    public void addParticipants() throws Exception {
        String uri = "/tournaments/1";
        Tournament tournament = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

        List<Participant> participants = new ArrayList<Participant>();
        participants.add(new Participant(tournament, "Random99"));
        participants.add(new Participant(tournament, "Random100"));

        tournamentManagerService.addParticipants(tournament, participants);

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Participants were added successfully");
    }

    @Test
    public void startTournament() throws Exception {
        String uri = "/tournaments/1";
        Tournament tournament = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

        tournamentManagerService.start(tournament);

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(tournament.isStarted());
    }

    @Test
    public void pauseTournament() throws Exception {
        String uri = "/tournaments/1";
        Tournament tournament = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

        tournamentManagerService.pause(tournament);

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(tournament.isOnHold());
    }

    @Test
    public void resumeTournament() throws Exception {
        String uri = "/tournaments/1";
        Tournament tournament = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

        tournamentManagerService.resume(tournament);

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertFalse(tournament.isOnHold());
    }

    @Test
    public void holdRound() throws Exception {
        String uri = "/tournaments/1";
        Tournament tournament = new Tournament("Kayle Mains Competition: Summoner's Gorge", 16);

        tournamentManagerService.addParticipant(tournament, "123");
        tournamentManagerService.addParticipant(tournament, "456");
        tournamentManagerService.addParticipant(tournament, "789");
        tournamentManagerService.addParticipant(tournament, "101112");

        tournamentManagerService.holdRound(tournament);

        String inputJson = super.mapToJson(tournament);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(tournament.getParticipants().size() == 2);
    }

    @Test
    public void deleteParticipant() throws Exception {
        String uri = "/tournaments/1/participants/1";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Participant is deleted successfully");
    }

    @Test
    public void deleteTournament() throws Exception {
        String uri = "/tournaments/1";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Tournament is deleted successfully");
    }
}
