package com.nba;

// a single game between 2 teams
public class Game {
    private Team homeTeam;
    private Team awayTeam;
    private boolean played;
    private Team winner;

    public Game(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.played = false;
        this.winner = null;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public boolean isPlayed() {
        return played;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
        this.played = true;
    }

}
