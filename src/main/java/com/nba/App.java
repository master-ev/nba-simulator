package com.nba;

public class App {
    public static void main(String[] args) throws Exception {
        Config config = Config.parse(args);
        SeasonData data = new SeasonData(config.season, 0.7);
        System.out.println("Season " + config.season + "    \n");
        if (config.analysis.equals("playoffs")) {
            Analysis.playoffProbabilites(data, config.useElo, config.sims);
        } else if (config.analysis.equals("title")) {
            Analysis.titleProbabilities(data, config.useElo, config.sims);
        } else if (config.analysis.equals("compare")) {
            Analysis.compareModels(data, config.sims);
        } else if (config.analysis.equals("tunek")) {
            Analysis.tuneK(data, config.sims);
        } else {
            System.out.println("Unknown analysis: " + config.analysis);
            System.out.println("Options: playoffs, title, compare");
        }
    }
}
