package com.nba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void newGameIsNotPlayed() {
        Team a = new Team(1, "A", "East");
        Team b = new Team(2, "B", "East");
        Game game = new Game(a, b);
        assertFalse(game.isPlayed());
    }

    @Test
    public void setWinnerMarksGamePlayed() {
        Team a = new Team(1, "A", "East");
        Team b = new Team(2, "B", "East");
        Game game = new Game(a, b);
        game.setWinner(a);
        assertTrue(game.isPlayed());
        assertEquals(a, game.getWinner());
    }

}
