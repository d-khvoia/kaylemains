package com.cybertournaments.kaylemains.manual_logic_testing;

import java.util.ArrayList;
import java.util.List;

public class KaylemainsApplication {

    private static TournamentService tournamentService = new TournamentService();

    public static void main(String[] args) {
        testTournament();
    }

    public static void testTournament() {

        Tournament kayleMainsTournament = new Tournament(1L,"Kayle Mains Competition: Summoner's Gorge", 16);

        List<Participant> participants = getTestParticipants(kayleMainsTournament);

        tournamentService.addParticipants(kayleMainsTournament, participants);
        tournamentService.start(kayleMainsTournament);

        while (!kayleMainsTournament.isFinished()) {
            System.out.println(kayleMainsTournament);
            printParticipants(kayleMainsTournament);
            printMatches(kayleMainsTournament);

            testRound(kayleMainsTournament);
        }

        System.out.println(kayleMainsTournament);
        printParticipants(kayleMainsTournament);
        printMatches(kayleMainsTournament);
    }

    private static void testRound(Tournament tournament) {
        System.out.println(tournamentService.holdRound(tournament));

    }

    private static void printParticipants(Tournament tournament) {
        for (Participant participant : tournament.getParticipants())
            System.out.println(participant);
    }

    private static void printMatches(Tournament tournament) {
        for (Match match : tournament.getMatches())
            System.out.println(match);
    }


    private static List<Participant> getTestParticipants(Tournament tournament) {
        List<Participant> participants = new ArrayList<Participant>();

        participants.add(new Participant(1L, tournament,"Dixon"));
        participants.add(new Participant(2L, tournament,"Turkis"));
        participants.add(new Participant(3L, tournament,"ArchMichaelWeb"));
        participants.add(new Participant(4L, tournament,"RandomKayleMain1"));
        participants.add(new Participant(5L, tournament,"RandomKayleMain2"));
        participants.add(new Participant(6L, tournament,"RandomKayleMain3"));
        participants.add(new Participant(7L, tournament,"RandomKayleMain4"));
        participants.add(new Participant(8L, tournament,"RandomKayleMain5"));
        participants.add(new Participant(9L, tournament,"RandomKayleMain6"));
        participants.add(new Participant(10L, tournament,"RandomKayleMain7"));
        participants.add(new Participant(11L, tournament,"RandomKayleMain8"));
        participants.add(new Participant(12L, tournament,"RandomKayleMain9"));
        participants.add(new Participant(13L, tournament,"RandomKayleMain10"));
        participants.add(new Participant(14L, tournament,"RandomKayleMain11"));
        participants.add(new Participant(15L, tournament,"RandomKayleMain12"));

        return participants;
    }
}
