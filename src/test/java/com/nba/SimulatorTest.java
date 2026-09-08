package com.nba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class SimulatorTest {
    @Test
    public void seriesAlwaysHasAWinner() {
        Team a = new Team(1, "Team A", "East");
        Team b = new Team(2, "Team B", "West");
        a.setRating(1600);
        b.setRating(1400);
        Simulator sim = new Simulator(true);
        for (int i = 0; i < 1000; i++) {
            Team winner = sim.simulateSeries(a, b);
            assertNotNull(winner);
            assertTrue(winner == a || winner == b);
        }
    }

    @Test
    public void winRateComputedCorrectly() {
        Team t = new Team(1, "Test", "East");
        for (int i = 0; i < 30; i++)
            t.addWin();
        for (int i = 0; i < 10; i++)
            t.addLoss();
        assertEquals(0.75, t.winRate(), 0.0001);
    }

    @Test
    public void playInReturnsEightSeeds() {
        Simulator sim = new Simulator(false);
        List<Team> ranked = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Team t = new Team(i, "T" + i, "East");
            for (int w = 0; w < (15 - i); w++) {
                t.addWin();
                t.resetSim();
            }
            ranked.add(t);
        }
        List<Team> seeds = sim.applyPlayIn(ranked);
        assertEquals(8, seeds.size());
    }
}
