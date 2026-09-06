package com.nba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EloTest {

    @Test
    public void equalRatingGiveFiftyPercent() {
        double prob = Elo.winProbability(1500, 1500);
        assertEquals(0.5, prob, 0.0001);
    }

    @Test
    public void higherRatingFavored() {
        double prob = Elo.winProbability(1600, 1400);
        assertTrue(prob > 0.5);
    }

    @Test
    public void ratingDifferenceIsSymmetric() {
        double probA = Elo.winProbability(1600, 1400);
        double probB = Elo.winProbability(1400, 1600);
        assertEquals(1.0, probA + probB, 0.0001);
    }
}
