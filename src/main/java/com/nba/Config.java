package com.nba;

public class Config {
    public int season = 2024;
    public String analysis = "playoffs";
    public int sims = 10000;
    public boolean useElo = false;

    public static Config parse(String[] args) {
        Config config = new Config();
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.equals("--season")) {
                config.season = Integer.parseInt(args[i + 1]);
                i = i + 2;
            } else if (arg.equals("--analysis")) {
                config.analysis = args[i + 1];
                i = i + 2;
            } else if (arg.equals("--sims")) {
                config.sims = Integer.parseInt(args[i + 1]);
                i = i + 2;
            } else if (arg.equals("--elo")) {
                config.useElo = true;
                i = i + 1;
            } else {
                System.out.println("Unknown argument: " + arg);
                i = i + 1;
            }
        }
        return config;
    }

}
