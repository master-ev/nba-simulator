package com.nba;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Backtest {
    public static Set<Team> actualPlayoffTeams(List<Team> teams) {
        List<Team> east = new ArrayList<>();
        List<Team> west = new ArrayList<>();
        for (Team team : teams) {
            if (team.getConference().equals("East")) {
                east.add(team);
            } else {
                west.add(team);
            }
        }
        east.sort(Comparator.comparingInt(Team::getWins).reversed());
        west.sort(Comparator.comparingInt(Team::getWins).reversed());
        Set<Team> playoffs = new HashSet<>();
        playoffs.addAll(east.subList(0, 8));
        playoffs.addAll(west.subList(0, 8));
        return playoffs;
    }

    public static void report(List<Team> teams, Map<Team, Integer> playoffCounts, Set<Team> actual, int numSeasons) {
        System.out.println("Team                       Predicted Actual");
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort(Comparator.comparingInt((Team t) -> playoffCounts.get(t)).reversed());
        for (Team team : sorted) {
            double prob = 100.0 * playoffCounts.get(team) / numSeasons;
            String madeIt = actual.contains(team) ? "IN" : "OUT";
            System.out.printf("%-25s %6.1f%%    %s%n", team.getName(), prob, madeIt);
        }
    }
}
