package com.cybertournaments.kaylemains.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "participantseq")
@Table(name = "PARTICIPANT")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    private Long id;
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonBackReference
    private Tournament tournament;

    private int totalScore = 0;

    public Participant() {
        nickname = "<empty>";
    }

    public Participant(Tournament tournament, String nickname) {
        this.tournament = tournament;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Participant))
            return false;
        Participant p = (Participant) o;
        return Objects.equals(id, p.id) && Objects.equals(nickname, p.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname);
    }

    @Override
    public String toString() {
        return "Participant # " + id + " | Nickname: " + nickname + " | Tournament ID: " + tournament.getId();
    }
}
