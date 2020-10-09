package com.cybertournaments.kaylemains.manual_logic_testing;

import java.util.Objects;

public class Participant {

    private Long id;
    private String nickname;

    private Tournament tournament;

    private int totalScore = 0;

    public Participant(Long id, Tournament tournament) {

        this.id = id;
        this.tournament = tournament;
        nickname = "<empty>";
    }

    public Participant(Long id, Tournament tournament, String nickname) {

        this.id = id;
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
