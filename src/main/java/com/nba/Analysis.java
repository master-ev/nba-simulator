package com.nba;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Analysis {
    public static void playoffProbabilites(SeasonData data, boolean useElo, int numSeasons) {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(data.getPlayedGames());
        Elo.computeRatings(data.getPlayedGames());
        Simulator sim = new Simulator(useElo);
        Map<Team, Integer> counts = sim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
        teams.sort(Comparator.comparingInt((Team t) -> counts.get(t)).reversed());
        System.out.println("Playoff probabilities:\n");
        for (Team team : teams) {
            double prob = 100.0 * counts.get(team) / numSeasons;
            System.out.printf("%-25s %.1f%%%n", team.getName(), prob);
        }
    }

    public static void titleProbabilities(SeasonData data, boolean useElo, int numSeasons) {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(data.getPlayedGames());
        Elo.computeRatings(data.getPlayedGames());
        Simulator sim = new Simulator(useElo);
        Map<Team, Integer> counts = sim.runTitleSimulations(teams, data.getRemainingGames(), numSeasons);
        teams.sort(Comparator.comparingInt((Team t) -> counts.get(t)).reversed());
        System.out.println("Title probabilities:\n");
        for (Team team : teams) {
            double prob = 100.0 * counts.get(team) / numSeasons;
            if (prob > 0.05) {
                System.out.printf("%-25s %.1f%%%n", team.getName(), prob);
            }
        }
    }

    public static void seedRatingsFromPreviousSeason(int previousSeason, Map<Integer, Team> teamsById)
            throws Exception {
        String cacheFile = "games-" + previousSeason + ".csv";
        java.io.File file = new java.io.File(cacheFile);
        List<Game> prevGames;
        if (file.exists()) {
            prevGames = GameStore.load(cacheFile, teamsById);
        } else {
            BallDontLieClient client = new BallDontLieClient();
            prevGames = client.getGames(previousSeason, teamsById);
            GameStore.save(prevGames, cacheFile);
        }
        Elo.computeRatings(prevGames);
        Elo.regressToMean(new java.util.ArrayList<>(teamsById.values()), 0.0);
    }

    // win rate vs Elo
    public static void compareModels(SeasonData data, int numSeasons) throws Exception {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(data.getPlayedGames());
        // tried seeding elo from the previous season here
        // Analysis.seedRatingsFromPreviousSeason(data.getSeason() - 1,
        // data.getTeamsById());
        Elo.computeRatings(data.getPlayedGames());
        Simulator winRateSim = new Simulator(false);
        Map<Team, Integer> winRateCounts = winRateSim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
        Simulator eloSim = new Simulator(true);
        Map<Team, Integer> eloCounts = eloSim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(data.getAllGames());
        Set<Team> actual = Backtest.actualPlayoffTeams(teams);
        double winRateBrier = Backtest.brierScore(teams, winRateCounts, actual, numSeasons);
        double eloBrier = Backtest.brierScore(teams, eloCounts, actual, numSeasons);
        System.out.println("Model comparison:\n");
        System.out.printf(" Win rate: %.4f%n", winRateBrier);
        System.out.printf(" Elo:      %.4f%n", eloBrier);
        System.out.println();
        Backtest.report(teams, winRateCounts, actual, numSeasons);
    }

    public static void tuneK(SeasonData data, int numSeasons) throws Exception {
        double[] kValues = { 5, 10, 20, 40, 60 };
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
        }
        Standings.compute(data.getAllGames());
        Set<Team> actual = Backtest.actualPlayoffTeams(teams);
        System.out.println("Tuning K:\n");
        for (double k : kValues) {
            Elo.setK(k);
            for (Team team : teams) {
                team.resetRealRecord();
                team.setRating(1500.0);
            }
            Standings.compute(data.getPlayedGames());
            Elo.computeRatings(data.getPlayedGames());
            Simulator sim = new Simulator(true);
            Map<Team, Integer> counts = sim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
            double brier = Backtest.brierScore(teams, counts, actual, numSeasons);
            System.out.printf(" K = %5.0f   %.4f%n", k, brier);
        }
    }

    public static void playoffChartHtml(SeasonData data, boolean useElo, int numSeasons) throws Exception {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
            team.setRating(1500.0);
        }
        Standings.compute(data.getPlayedGames());
        Elo.computeRatings(data.getPlayedGames());
        Simulator sim = new Simulator(useElo);
        Map<Team, Integer> counts = sim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
        teams.sort(Comparator.comparingInt((Team t) -> counts.get(t)).reversed());
        HtmlReport.playoffChart(teams, counts, numSeasons, "playoff-report.html");
        System.out.println("Wrote playoff-report.html");
    }

    public static void seedHeatmapHtml(SeasonData data, boolean useElo, int numSeasons) throws Exception {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
            team.setRating(1500.0);
        }
        Standings.compute(data.getPlayedGames());
        Elo.computeRatings(data.getPlayedGames());
        Simulator sim = new Simulator(useElo);
        Map<Team, int[]> seedCounts = sim.runSeedSimulations(teams, data.getRemainingGames(), numSeasons);
        List<Team> east = new java.util.ArrayList<>();
        for (Team team : teams) {
            if (team.getConference().equals("East")) {
                east.add(team);
            }
        }
        east.sort(Comparator.comparingInt((Team t) -> seedCounts.get(t)[1]).reversed());
        HtmlReport.seedHeatmap(east, seedCounts, numSeasons, 8, "seed-heatmap.html");
        System.out.println("Wrote seed-heatmap.html");
    }

    public static void generateReports(SeasonData data, boolean useElo, int numSeasons) throws Exception {
        List<Team> teams = data.getTeams();
        for (Team team : teams) {
            team.resetRealRecord();
            team.setRating(1500.0);
        }
        Standings.compute(data.getPlayedGames());
        Elo.computeRatings(data.getPlayedGames());
        Simulator sim = new Simulator(useElo);
        Map<Team, Integer> playoffCounts = sim.runManySeasons(teams, data.getRemainingGames(), numSeasons);
        List<Team> byPlayoff = new java.util.ArrayList<>(teams);
        byPlayoff.sort(Comparator.comparingInt((Team t) -> playoffCounts.get(t)).reversed());
        HtmlReport.playoffChart(byPlayoff, playoffCounts, numSeasons, "playoff-report.html");
        Map<Team, int[]> seedCounts = sim.runSeedSimulations(teams, data.getRemainingGames(), numSeasons);
        List<Team> east = new java.util.ArrayList<>();
        List<Team> west = new java.util.ArrayList<>();
        for (Team team : teams) {
            if (team.getConference().equals("East"))
                east.add(team);
            else
                west.add(team);
        }
        east.sort(Comparator.comparingInt((Team t) -> seedCounts.get(t)[1]).reversed());
        west.sort(Comparator.comparingInt((Team t) -> seedCounts.get(t)[1]).reversed());
        HtmlReport.seedHeatmapFull(east, west, seedCounts, numSeasons, 8, "seed-heatmap.html");
        HtmlReport.writeIndex(data.getSeason(), "index.html");
        System.out.println("Wrote index.html, playoff-report.html, seed-heatmap.html");
    }
}
