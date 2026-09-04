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
        Standings.compute(playedGames);
        List<Team> teams = new ArrayList<>(teamsById.values());
        Simulator sim = new Simulator();
        sim.simulateSeason(teams, remainingGames);
        teams.sort(Comparator.comparingInt(Team::getSimWins).reversed());
        System.out.println("Simulated final standings:\n");
        for (Team team : teams) {
            System.out.println(team.getName() + ": " + team.getSimWins() + "W " + team.getSimLosses() + "L");
        }
    }
}
