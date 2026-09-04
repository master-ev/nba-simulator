package com.nba;

public class Team {
    private int id;
    private String name;
    private int wins;
    private int losses;
    private int simWins;
    private int simLosses;

    public void resetSim() {
        this.simWins = wins;
        this.simLosses = losses;
    }

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public int getId() {
        return id;
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

    public double winRate() {
        int total = wins + losses;
        if (total == 0) {
            return 0.5;
        }
        return (double) wins / total;
    }

    public void addSimWin() {
        this.simWins = this.simWins + 1;
    }

    public void addSimLoss() {
        this.simLosses = this.simLosses + 1;
    }

    public int getSimWins() {
        return simWins;
    }

    public int getSimLosses() {
        return simLosses;
    }
}
