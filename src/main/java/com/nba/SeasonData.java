package com.nba;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonData {
    private Map<Integer, Team> teamsById;
    private List<Team> teams;
    private List<Game> allGames;
    private List<Game> playedGames;
    private List<Game> remainingGames;

    public SeasonData(int season, double cutoffFraction) throws Exception {
        BallDontLieClient client = new BallDontLieClient();
        this.teamsById = client.getTeamsById();
        this.teams = new ArrayList<>(teamsById.values());
        String cacheFile = "games-" + season + ".csv";
        java.io.File file = new java.io.File(cacheFile);
        if (file.exists()) {
            this.allGames = GameStore.load(cacheFile, teamsById);
        } else {
            this.allGames = client.getGames(season, teamsById);
            GameStore.save(allGames, cacheFile);
        }

        int cutoff = (int) (allGames.size() * cutoffFraction);
        this.playedGames = allGames.subList(0, cutoff);
        this.remainingGames = allGames.subList(cutoff, allGames.size());
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Game> getAllGames() {
        return allGames;
    }

    public List<Game> getPlayedGames() {
        return playedGames;
    }

    public List<Game> getRemainingGames() {
        return remainingGames;
    }

}
