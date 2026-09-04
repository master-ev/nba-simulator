package com.nba;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Standings.compute(games);
        List<Team> ranked = Standings.sortedByWins(new ArrayList<>(teamsById.values()));
        System.out.println("\nStandings (from " + games.size() + " games):\n");
        for (Team team : ranked) {
            System.out.println(team);
        }

        Simulator sim = new Simulator();
        Team a = teamsById.get(21);
        Team b = teamsById.get(29);
        int aWins = 0;
        int trials = 10000;
        for (int i = 0; i < trials; i++) {
            if (sim.simulateGame(a, b) == a) {
                aWins = aWins + 1;
            }
        }
        System.out.println("\nSimulating " + a.getName() + " vs " + b.getName() + ":");
        System.out.println(a.getName() + " win rate: " + a.winRate());
        System.out.println(b.getName() + " win rate: " + b.winRate());
        System.out.println(a.getName() + " won " + aWins + " of " + trials + " (" + (100.0 * aWins / trials) + "%)");
    }
}
