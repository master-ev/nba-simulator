package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Standings {
    public static void compute(List<Game> games) {
        for (Game game : games) {
            if (!game.isPlayed()) {
                continue;
            }
            Team winner = game.getWinner();
            Team loser;
            if (winner == game.getHomeTeam()) {
                loser = game.getAwayTeam();
            } else {
                loser = game.getHomeTeam();
            }
            winner.addWin();
            loser.addLoss();
        }
    }

    public static List<Team> sortedByWins(List<Team> teams) {
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort(Comparator.comparingInt(Team::getWins).reversed());
        return sorted;
    }
}
