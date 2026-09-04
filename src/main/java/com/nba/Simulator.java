package com.nba;

import java.util.List;
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

    public void simulateSeason(List<Team> teams, List<Game> gamesToSimulate) {
        for (Team team : teams) {
            team.resetSim();
        }

        for (Game game : gamesToSimulate) {
            Team home = game.getHomeTeam();
            Team away = game.getAwayTeam();
            Team winner = simulateGame(home, away);
            if (winner == home) {
                home.addSimWin();
                away.addSimLoss();
            } else {
                away.addSimWin();
                home.addSimLoss();
            }
        }
    }
}
