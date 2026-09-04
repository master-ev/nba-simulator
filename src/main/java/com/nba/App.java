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
    }
}
