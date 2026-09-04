package com.nba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStore {
    public static void save(List<Game> games, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (Game game : games) {
            int homeId = game.getHomeTeam().getId();
            int awayId = game.getAwayTeam().getId();
            int winnerId = 0;
            if (game.isPlayed()) {
                winnerId = game.getWinner().getId();
            }
            writer.write(homeId + "," + awayId + "," + winnerId + "\n");
        }
        writer.close();
    }

    public static List<Game> load(String filename, Map<Integer, Team> teamsById) throws IOException {
        List<Game> games = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int homeId = Integer.parseInt(parts[0]);
            int awayId = Integer.parseInt(parts[1]);
            int winnerId = Integer.parseInt(parts[2]);
            Team home = teamsById.get(homeId);
            Team away = teamsById.get(awayId);
            Game game = new Game(home, away);
            if (winnerId != 0) {
                Team winner = teamsById.get(winnerId);
                game.setWinner(winner);
            }
            games.add(game);
        }
        reader.close();
        return games;
    }
}
