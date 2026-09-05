package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Simulator {
    private Random random;
    private boolean useElo;

    public Simulator(boolean useElo) {
        this.random = new Random();
        this.useElo = useElo;
    }

    public Team simulateGame(Team home, Team away) {
        double homeWinProb;
        if (useElo) {
            homeWinProb = Elo.winProbability(home.getRating(), away.getRating());
        } else {
            double homeStrength = home.winRate();
            double awayStrength = away.winRate();
            homeWinProb = homeStrength / (homeStrength + awayStrength);
        }
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

    public Team simulateSeries(Team a, Team b) {
        int aWins = 0;
        int bWins = 0;
        while (aWins < 4 && bWins < 4) {
            Team gameWinner = simulateGame(a, b);
            if (gameWinner == a) {
                aWins = aWins + 1;
            } else {
                bWins = bWins + 1;
            }
        }
        if (aWins == 4) {
            return a;
        } else {
            return b;
        }
    }

    public Team simulateConference(List<Team> seeds) {
        Team w1 = simulateSeries(seeds.get(0), seeds.get(7));
        Team w2 = simulateSeries(seeds.get(1), seeds.get(6));
        Team w3 = simulateSeries(seeds.get(2), seeds.get(5));
        Team w4 = simulateSeries(seeds.get(3), seeds.get(4));
        Team s1 = simulateSeries(w1, w4);
        Team s2 = simulateSeries(w2, w3);
        return simulateSeries(s1, s2);
    }

    public Team simulatePlayoffs(List<Team> teams) {
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
        List<Team> eastSeeds = east.subList(0, 8);
        List<Team> westSeeds = west.subList(0, 8);
        Team eastChamp = simulateConference(eastSeeds);
        Team westChamp = simulateConference(westSeeds);
        return simulateSeries(eastChamp, westChamp);
    }

    public Map<Team, Integer> runTitleSimulations(List<Team> teams, List<Game> remainingGames, int numSeasons) {
        Map<Team, Integer> titleCounts = new HashMap<>();
        for (Team team : teams) {
            titleCounts.put(team, 0);
        }
        for (int season = 0; season < numSeasons; season++) {
            simulateSeason(teams, remainingGames);
            Team champion = simulatePlayoffs(teams);
            titleCounts.put(champion, titleCounts.get(champion) + 1);
        }
        return titleCounts;
    }

    public Map<Team, int[]> runSeedSimulations(List<Team> teams, List<Game> remainingGames, int numSeasons) {
        Map<Team, int[]> seedCounts = new HashMap<>();
        for (Team team : teams) {
            seedCounts.put(team, new int[16]);
        }
        for (int season = 0; season < numSeasons; season++) {
            simulateSeason(teams, remainingGames);
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
            recordSeeds(east, seedCounts);
            recordSeeds(west, seedCounts);
        }
        return seedCounts;
    }

    private void recordSeeds(List<Team> ranked, Map<Team, int[]> seedCounts) {
        for (int i = 0; i < ranked.size(); i++) {
            Team team = ranked.get(i);
            int seed = i + 1;
            seedCounts.get(team)[seed]++;
        }
    }
}
