package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        BallDontLieClient client = new BallDontLieClient();
        Map<Integer, Team> teamsById = client.getTeamsById();
        String cacheFile = "games-2024.csv";
        List<Game> games;
        java.io.File file = new java.io.File(cacheFile);
        if (file.exists()) {
            System.out.println("Loading games from cache...");
            games = GameStore.load(cacheFile, teamsById);
        } else {
            System.out.println("Fetching games from API...");
            games = client.getGames(2024, teamsById);
            GameStore.save(games, cacheFile);
            System.out.println("Saved to cache.");
        }
        int cutoff = (int) (games.size() * 0.7);
        List<Game> playedGames = games.subList(0, cutoff);
        List<Game> remainingGames = games.subList(cutoff, games.size());
        System.out.println("Cutoff: " + cutoff + " games played, " + remainingGames.size() + " to simulate\n");
        List<Team> teams = new ArrayList<>(teamsById.values());
        Standings.compute(playedGames);
        Elo.computeRatings(playedGames);
        Simulator sim = new Simulator();
        int numSeasons = 10000;
        Map<Team, Integer> playoffCounts = sim.runManySeasons(teams, remainingGames, numSeasons);
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(games);
        Set<Team> actual = Backtest.actualPlayoffTeams(teams);
        Backtest.report(teams, playoffCounts, actual, numSeasons);
        List<Team> byRating = new ArrayList<>(teams);
        byRating.sort(Comparator.comparingDouble(Team::getRating).reversed());
        System.out.println("Elo ratings at cutoff:\n");
        for (Team team : byRating) {
            System.out.printf("%-25s %.0f%n", team.getName(), team.getRating());
        }
        System.out.println();
    }
}
