package com.nba;

public class Team {
    private String name;
    private int wins;
    private int losses;

    public Team(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addWin() {
        this.wins = this.wins + 1;
    }

    public void addLoss() {
        this.losses = this.losses + 1;
    }

    public String toString() {
        return name + ": " + wins + "W " + losses + "L";
    }
}
