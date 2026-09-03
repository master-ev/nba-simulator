package com.nba;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        BallDontLieClient client = new BallDontLieClient();
        Map<Integer, Team> teamsById = client.getTeamsById();
        List<Game> games = client.getGames(2024, teamsById);
        Standings.compute(games);
        List<Team> teams = new ArrayList<>(teamsById.values());
        List<Team> ranked = Standings.sortedByWins(teams);
        System.out.println("Standings (from " + games.size() + " games):\n");
        for (Team team : ranked) {
            System.out.println(team);
        }
    }
}
