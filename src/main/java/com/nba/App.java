package com.nba;

import java.util.ArrayList;
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
        int numSeasons = 10000;
        Simulator winRateSim = new Simulator(false);
        Map<Team, Integer> winRateCounts = winRateSim.runManySeasons(teams, remainingGames, numSeasons);
        Simulator eloSim = new Simulator(true);
        Map<Team, Integer> eloCounts = eloSim.runManySeasons(teams, remainingGames, numSeasons);
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(games);
        Set<Team> actual = Backtest.actualPlayoffTeams(teams);
        double winRateBrier = Backtest.brierScore(teams, winRateCounts, actual, numSeasons);
        double eloBrier = Backtest.brierScore(teams, eloCounts, actual, numSeasons);
        System.out.println("Model comparison\n");
        System.out.printf(" Win rate: %.4f%n", winRateBrier);
        System.out.printf(" Elo:      %.4f%n", eloBrier);
        if (eloBrier < winRateBrier) {
            System.out.println("\nElo is better.");
        } else {
            System.out.println("\nWin rate is better.");
        }
    }
}
