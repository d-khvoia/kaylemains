package com.cybertournaments.kaylemains.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Entity
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "matchseq")
@Table(name = "MATCH")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonBackReference
    private Tournament tournament;

    private boolean isFinished;

    @Transient
    private Participant firstParticipant;
    @Transient
    private Participant secondParticipant;

    private int firstParticipantScore;
    private int secondParticipantScore;

    //These fields are needed to get valid match summary after losers removal from ArrayList of participants.
    private Long firstParticipantID, secondParticipantID;
    private String firstParticipantNickname, secondParticipantNickname;

    private LocalDateTime startTime, finishTime;

    public Match() { }

    public Match(Tournament tournament, Participant firstParticipant, Participant secondParticipant, LocalDateTime startTime, LocalDateTime finishTime) {
        this.tournament = tournament;
        isFinished = false;
        this.firstParticipant = firstParticipant;
        this.secondParticipant = secondParticipant;
        this.startTime = startTime;
        this.finishTime = finishTime;

        firstParticipantID = firstParticipant.getId();
        secondParticipantID = secondParticipant.getId();
        firstParticipantNickname = firstParticipant.getNickname();
        secondParticipantNickname = secondParticipant.getNickname();
    }

    public String getSummary() {
        String tournamentID = "Tournament ID: " + tournament.getId() + " | ";
        String matchID = "Match # " + id;
        String firstParticipant = "Participant # " + firstParticipantID + " | Nickname: " + firstParticipantNickname;
        String secondParticipant = "Participant # " + secondParticipantID + " | Nickname: " + secondParticipantNickname;

        if (isFinished) {
            matchID += " is finished.\n";
            firstParticipant += " — " + firstParticipantScore + "\n";
            secondParticipant += " — " + secondParticipantScore + "\n";
        } else {
            matchID += " is still in progress...\n";
            firstParticipant += "\n";
            secondParticipant += "\n";
        }

        return tournamentID + matchID + firstParticipant + secondParticipant;
    }

    //Simulating a match
    public void start() {
        Random random = new Random();
        firstParticipantScore = random.nextInt(2);

        if (firstParticipantScore == 1) {
            secondParticipantScore = 0;
            firstParticipant.setTotalScore(firstParticipant.getTotalScore() + 1);
        }
        else {
            secondParticipantScore = 1;
            secondParticipant.setTotalScore(secondParticipant.getTotalScore() + 1);
        }

        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Participant getFirstParticipant() {
        return firstParticipant;
    }

    public void setFirstParticipant(Participant firstParticipant) {
        this.firstParticipant = firstParticipant;
    }

    public Participant getSecondParticipant() {
        return secondParticipant;
    }

    public void setSecondParticipant(Participant secondParticipant) {
        this.secondParticipant = secondParticipant;
    }

    public int getFirstParticipantScore() {
        return firstParticipantScore;
    }

    public void setFirstParticipantScore(int firstParticipantScore) {
        this.firstParticipantScore = firstParticipantScore;
    }

    public int getSecondParticipantScore() {
        return secondParticipantScore;
    }

    public void setSecondParticipantScore(int secondParticipantScore) {
        this.secondParticipantScore = secondParticipantScore;
    }

    public Long getFirstParticipantID() {
        return firstParticipantID;
    }

    public void setFirstParticipantID(Long firstParticipantID) {
        this.firstParticipantID = firstParticipantID;
    }

    public Long getSecondParticipantID() {
        return secondParticipantID;
    }

    public void setSecondParticipantID(Long secondParticipantID) {
        this.secondParticipantID = secondParticipantID;
    }

    public String getFirstParticipantNickname() {
        return firstParticipantNickname;
    }

    public void setFirstParticipantNickname(String firstParticipantNickname) {
        this.firstParticipantNickname = firstParticipantNickname;
    }

    public String getSecondParticipantNickname() {
        return secondParticipantNickname;
    }

    public void setSecondParticipantNickname(String secondParticipantNickname) {
        this.secondParticipantNickname = secondParticipantNickname;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Match))
            return false;
        Match m = (Match) o;
        return Objects.equals(id, m.id) && Objects.equals(isFinished, m.isFinished)
               && Objects.equals(firstParticipantScore, m.firstParticipantScore)
               && Objects.equals(secondParticipantScore, m.secondParticipantScore)
               && Objects.equals(firstParticipantID, m.firstParticipantID)
               && Objects.equals(secondParticipantID, m.secondParticipantID)
               && Objects.equals(firstParticipantNickname, m.firstParticipantNickname)
               && Objects.equals(secondParticipantNickname, m.secondParticipantNickname)
               && Objects.equals(startTime, m.startTime)
               && Objects.equals(finishTime, m.finishTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isFinished, firstParticipantScore, secondParticipantScore,
                            firstParticipantID, secondParticipantID, firstParticipantNickname,
                            secondParticipantNickname, startTime, finishTime);
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
