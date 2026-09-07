package com.nba;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GameStoreTest {

    @Test
    public void saveAndLoadRoundTrip() throws Exception {
        Team a = new Team(1, "A", "East");
        Team b = new Team(2, "B", "West");
        Map<Integer, Team> teamsById = new HashMap<>();
        teamsById.put(1, a);
        teamsById.put(2, b);
        List<Game> original = new ArrayList<>();
        Game played = new Game(a, b);
        played.setWinner(a);
        original.add(played);
        original.add(new Game(b, a)); // not played
        String tmp = "test-games.csv";
        GameStore.save(original, tmp);
        List<Game> loaded = GameStore.load(tmp, teamsById);
        new File(tmp).delete();
        assertEquals(2, loaded.size());
        assertTrue(loaded.get(0).isPlayed());
        assertEquals(a, loaded.get(0).getWinner());
        assertTrue(!loaded.get(1).isPlayed());
    }
}
