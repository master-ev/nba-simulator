package com.nba;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class BacktestTest {
    @Test
    public void sixteenTeamsMakePlayoffs() {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Team east = new Team(i, "East" + i, "East");
            Team west = new Team(100 + i, "West" + i, "West");
            for (int w = 0; w < (15 - i); w++) {
                east.addWin();
                west.addWin();
            }
            teams.add(east);
            teams.add(west);
        }
        Set<Team> playoffs = Backtest.actualPlayoffTeams(teams);
        assertEquals(16, playoffs.size());
    }
}
