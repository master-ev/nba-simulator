package com.nba;

public class App {
    public static void main(String[] args) {
        Team lakers = new Team("Lakers");
        Team sixers = new Team("Sixers");
        Game game = new Game(lakers, sixers);
        System.out.println("Home: " + game.getHomeTeam().getName());
        System.out.println("Away: " + game.getAwayTeam().getName());
        System.out.println("Played? " + game.isPlayed());
        game.setWinner(lakers);
        lakers.addWin();
        sixers.addLoss();
        System.out.println();
        System.out.println("Played? " + game.isPlayed());
        System.out.println("Winner: " + game.getWinner().getName());
        System.out.println(lakers);
        System.out.println(sixers);
    }
}
