package com.nba;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

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
            String name = node.get("full_name").asText();
            teams.add(new Team(name));
        }
        return teams;
    }
}
