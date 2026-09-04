package com.nba;

import java.util.Random;

public class Simulator {
    private Random random;

    public Simulator() {
        this.random = new Random();
    }

    public Team simulateGame(Team home, Team away) {
        double homeStrength = home.winRate();
        double awayStrength = away.winRate();
        double homeWinProb = homeStrength / (homeStrength + awayStrength);
        if (random.nextDouble() < homeWinProb) {
            return home;
        } else {
            return away;
        }
    }
}
