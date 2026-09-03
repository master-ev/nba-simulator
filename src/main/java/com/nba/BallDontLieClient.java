package com.nba;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BallDontLieClient {
    private static final String BASE_URL = "https://api.balldontlie.io/v1";
    private final HttpClient client;
    private final String apiKey;

    public BallDontLieClient() {
        this.client = HttpClient.newHttpClient();
        this.apiKey = System.getenv("BALLDONTLIE_API_KEY");
    }

    public String get(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", apiKey)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API returned status " + response.statusCode());
        }
        return response.body();
    }

    public List<Team> getTeams() throws Exception {
        String json = get("/teams");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode data = root.get("data");
        List<Team> teams = new ArrayList<>();
        for (JsonNode node : data) {
            String conference = node.get("conference").asText().trim();
            if (conference.isEmpty()) {
                continue;
            }
            int id = node.get("id").asInt();
            String name = node.get("full_name").asText();
            teams.add(new Team(id, name));
        }
        return teams;
    }

    public Map<Integer, Team> getTeamsById() throws Exception {
        List<Team> teams = getTeams();
        Map<Integer, Team> byId = new HashMap<>();
        for (Team team : teams) {
            byId.put(team.getId(), team);
        }
        return byId;
    }

    public List<Game> getGames(int season, Map<Integer, Team> teamsById) throws Exception {
        String json = get("/games?seasons[]=" + season + "&per_page=100");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode data = root.get("data");
        List<Game> games = new ArrayList<>();
        for (JsonNode node : data) {
            int homeId = node.get("home_team").get("id").asInt();
            int awayId = node.get("visitor_team").get("id").asInt();
            Team home = teamsById.get(homeId);
            Team away = teamsById.get(awayId);
            if (home == null || away == null) {
                continue;
            }
            Game game = new Game(home, away);
            String status = node.get("status").asText();
            if (status.equals("Final")) {
                int homeScore = node.get("home_team_score").asInt();
                int awayScore = node.get("visitor_team_score").asInt();
                if (homeScore > awayScore) {
                    game.setWinner(home);
                } else {
                    game.setWinner(away);
                }
            }
            games.add(game);
        }
        return games;
    }
}
