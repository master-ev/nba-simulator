package com.nba;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StandingsTest {
    @Test
    public void winsAndLossesCountedCorrectly() {
        Team a = new Team(1, "A", "East");
        Team b = new Team(2, "B", "East");
        List<Game> games = new ArrayList<>();
        Game g1 = new Game(a, b);
        g1.setWinner(a);
        games.add(g1);
        Game g2 = new Game(a, b);
        g2.setWinner(a);
        games.add(g2);
        Game g3 = new Game(a, b);
        g3.setWinner(a);
        games.add(g3);
        Game g4 = new Game(a, b);
        g4.setWinner(b);
        games.add(g4);
        Standings.compute(games);
        assertEquals(3, a.getWins());
        assertEquals(1, a.getLosses());
        assertEquals(1, b.getWins());
        assertEquals(3, b.getLosses());
    }

    @Test
    public void unplayedGamesAreIgnored() {
        Team a = new Team(1, "A", "East");
        Team b = new Team(2, "B", "East");
        List<Game> games = new ArrayList<>();
        games.add(new Game(a, b));
        Standings.compute(games);
        assertEquals(0, a.getWins());
        assertEquals(0, b.getWins());
    }
}
