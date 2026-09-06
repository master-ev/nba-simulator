package com.nba;

public class App {
    public static void main(String[] args) throws Exception {
        int season = 2025;
        int numSeasons = 10000;
        SeasonData data = new SeasonData(season, 0.7);
        System.out.println("    Season " + season + "   \n");
        Analysis.playoffProbabilites(data, false, numSeasons);
        System.out.println();
        Analysis.titleProbabilities(data, false, numSeasons);
        System.out.println();
        Analysis.compareModels(data, numSeasons);
    }
}
