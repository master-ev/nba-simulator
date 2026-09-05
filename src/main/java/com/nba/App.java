package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
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
        int cutoff = (int) (games.size() * 0.7);
        List<Game> playedGames = games.subList(0, cutoff);
        List<Game> remainingGames = games.subList(cutoff, games.size());
        System.out.println("Cutoff: " + cutoff + " games played, " + remainingGames.size() + " to simulate\n");
        List<Team> teams = new ArrayList<>(teamsById.values());
        Standings.compute(playedGames);
        Elo.computeRatings(playedGames);
        int numSeasons = 10000;
        Simulator sim = new Simulator(false);
        Map<Team, int[]> seedCounts = sim.runSeedSimulations(teams, remainingGames, numSeasons);
        List<Team> east = new ArrayList<>();
        for (Team team : teams) {
            if (team.getConference().equals("East")) {
                east.add(team);
            }
        }
        east.sort(Comparator.comparingInt((Team t) -> seedCounts.get(t)[1]).reversed());
        System.out.println("East seed distribution:\n");
        System.out.printf("%-25s", "Team");
        for (int seed = 1; seed <= 8; seed++) {
            System.out.printf("%6d", seed);
        }
        System.out.println();
        for (Team team : east) {
            System.out.printf("%-25s", team.getName());
            for (int seed = 1; seed <= 8; seed++) {
                double pct = 100.0 * seedCounts.get(team)[seed] / numSeasons;
                System.out.printf("%5.0f%%", pct);
            }
            System.out.println();
        }
    }
}
