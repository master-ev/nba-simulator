package com.nba;

import java.util.List;

public class Elo {
    public static final double K = 20.0;

    public static double winProbability(double ratingA, double ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    public static double updateRating(double rating, double expected, double result) {
        return rating + K * (result - expected);
    }

    public static void computeRatings(List<Game> playedGames) {
        for (Game game : playedGames) {
            if (!game.isPlayed()) {
                continue;
            }
            Team home = game.getHomeTeam();
            Team away = game.getAwayTeam();
            double homeExpected = winProbability(home.getRating(), away.getRating());
            double awayExpected = 1.0 - homeExpected;
            double homeResult;
            double awayResult;
            if (game.getWinner() == home) {
                homeResult = 1.0;
                awayResult = 0.0;
            } else {
                homeResult = 0.0;
                awayResult = 1.0;
            }
            home.setRating(updateRating(home.getRating(), homeExpected, homeResult));
            away.setRating(updateRating(away.getRating(), awayExpected, awayResult));
        }
    }
}
