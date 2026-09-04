package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Simulator {
    private Random random;

    public Simulator() {
        this.random = new Random();
    }

    public Team simulateGame(Team home, Team away) {
        double homeWinProb = Elo.winProbability(home.getRating(), away.getRating());
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

    public Map<Team, Integer> runManySeasons(List<Team> teams, List<Game> remainingGames, int numSeasons) {
        Map<Team, Integer> playoffCounts = new HashMap<>();
        for (Team team : teams) {
            playoffCounts.put(team, 0);
        }
        for (int season = 0; season < numSeasons; season++) {
            simulateSeason(teams, remainingGames);
            List<Team> playoffTeams = getPlayoffTeams(teams);
            for (Team team : playoffTeams) {
                playoffCounts.put(team, playoffCounts.get(team) + 1);
            }
        }
        return playoffCounts;
    }

    private List<Team> getPlayoffTeams(List<Team> teams) {
        List<Team> east = new ArrayList<>();
        List<Team> west = new ArrayList<>();
        for (Team team : teams) {
            if (team.getConference().equals("East")) {
                east.add(team);
            } else {
                west.add(team);
            }
        }
        east.sort(Comparator.comparingInt(Team::getSimWins).reversed());
        west.sort(Comparator.comparingInt(Team::getSimWins).reversed());
        List<Team> playoffs = new ArrayList<>();
        playoffs.addAll(east.subList(0, 8));
        playoffs.addAll(west.subList(0, 8));
        return playoffs;
    }
}
