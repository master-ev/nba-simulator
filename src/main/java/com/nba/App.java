package com.nba;

import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        BallDontLieClient client = new BallDontLieClient();
        List<Team> teams = client.getTeams();
        System.out.println("Loaded " + teams.size() + " teams:");
        for (Team team : teams) {
            System.out.println(team);
        }
    }
}
