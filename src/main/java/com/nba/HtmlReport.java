package com.nba;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HtmlReport {
    public static void playoffChart(List<Team> teams, Map<Team, Integer> counts, int numSeasons, String filename)
            throws IOException {
        FileWriter w = new FileWriter(filename);
        w.write("<!DOCTYPE html>\n");
        w.write("<html><head><meta charset='utf-8'>\n");
        w.write("<title>Playoff Probabilities</title>\n");
        w.write("<link href='https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;600;700&display=swap' rel='stylesheet'>\n");
        w.write("<style>\n");
        w.write("* { box-sizing: border-box; margin: 0; }\n");
        w.write("body { font-family: 'Space Grotesk', sans-serif; ");
        w.write("background: radial-gradient(circle at 20% 0%, #1a0b2e 0%, #0a0a0f 55%), ");
        w.write("linear-gradient(135deg, #0d1f14 0%, #0a0a0f 60%); ");
        w.write("background-blend-mode: screen; color: #f0f0f5; padding: 60px 50px; min-height: 100vh; }\n");
        w.write("h1 { font-size: 42px; font-weight: 700; letter-spacing: -1px; margin-bottom: 8px; }\n");
        w.write(".sub { color: #8a8aa0; font-size: 15px; margin-bottom: 40px; }\n");
        w.write(".row { display: flex; align-items: center; margin: 6px 0; }\n");
        w.write(".name { width: 210px; text-align: right; padding-right: 16px; font-size: 14px; color: #c8c8d8; }\n");
        w.write(".bar { height: 22px; border-radius: 4px; }\n");
        w.write(".pct { padding-left: 10px; font-size: 13px; color: #8a8aa0; font-weight: 600; }\n");
        w.write("</style></head><body>\n");
        w.write("<h1>Playoff Probabilities</h1>\n");
        w.write("<div class='sub'>Monte Carlo — 10,000 simulated seasons</div>\n");
        w.write("<h1>Playoff Probabilities</h1>\n");
        for (Team team : teams) {
            double prob = 100.0 * counts.get(team) / numSeasons;
            // low to high
            int r = clamp((int) (150 - prob * 1.1));
            int g = clamp((int) (80 + prob * 1.5));
            int b = clamp((int) (200 - prob * 1.6));
            String color = "rgb(" + r + "," + g + "," + b + ")";
            w.write("<div class='row'>\n");
            w.write("   <div class='name'>" + team.getName() + "</div>\n");
            w.write("   <div class='bar' style='width:" + (prob * 4) + "px; background:" + color + ";'></div>\n");
            w.write("   <div class='pct'>" + String.format("%.1f%%", prob) + "</div>\n");
            w.write("</div>\n");
        }
        w.write("</body></html>\n");
        w.close();
    }

    public static void seedHeatmap(List<Team> teams, Map<Team, int[]> seedCounts, int numSeasons, int maxSeed,
            String filename) throws IOException {
        FileWriter w = new FileWriter(filename);
        w.write("<!DOCTYPE html>\n");
        w.write("<html><head><meta charset='utf-8'>\n");
        w.write("<title>Seed Distribution</title>\n");
        w.write("<style>\n");
        w.write("body { font-family: sans-serif; background: #111; color: #eee; padding: 30px; }\n");
        w.write("table { border-collapse: collapse; }\n");
        w.write("td, th { width: 44px; height: 30px; text-align: center; font-size: 12px; }\n");
        w.write("th.name, td.name { width: 190px; text-align: right; padding-right: 10px; }\n");
        w.write("</style></head><body>\n");
        w.write("<h1>Seed Distribution</h1>\n");
        w.write("<table>\n");
        w.write("<tr><th class='name'>Team</th>");
        for (int seed = 1; seed <= maxSeed; seed++) {
            w.write("<th>" + seed + "</th>");
        }
        w.write("</tr>\n");
        for (Team team : teams) {
            w.write("<tr><td class='name'>" + team.getName() + "</td>");
            for (int seed = 1; seed <= maxSeed; seed++) {
                double pct = 100.0 * seedCounts.get(team)[seed] / numSeasons;
                int intensity = (int) (pct / 100.0 * 255);
                String bg = "rgb(" + (intensity / 4) + "," + (intensity / 2) + "," + intensity + ")";
                String label = pct >= 1.0 ? String.format("%.0f", pct) : "";
                w.write("<td style='background:" + bg + ";'>" + label + "</td>");
            }
            w.write("</tr>\n");
        }
        w.write("</table>\n</body></html>\n");
        w.close();
    }

    public static void seedHeatmapFull(List<Team> east, List<Team> west, Map<Team, int[]> seedCounts, int numSeasons,
            int maxSeed, String filename) throws IOException {
        FileWriter w = new FileWriter(filename);
        w.write("<!DOCTYPE html>\n<html><head><meta charset='utf-8'>\n");
        w.write("<title>Seed Distribution</title>\n");
        w.write("<link href='https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;600;700&display=swap' rel='stylesheet'>\n");
        w.write("<style>\n");
        w.write("* { box-sizing: border-box; margin: 0; }\n");
        w.write("body { font-family: 'Space Grotesk', sans-serif; ");
        w.write("background: radial-gradient(circle at 20% 0%, #1a0b2e 0%, #0a0a0f 55%), ");
        w.write("linear-gradient(135deg, #0d1f14 0%, #0a0a0f 60%); ");
        w.write("background-blend-mode: screen; color: #f0f0f5; padding: 60px 50px; }\n");
        w.write("h1 { font-size: 42px; font-weight: 700; letter-spacing: -1px; }\n");
        w.write("h2 { font-size: 20px; font-weight: 600; margin: 30px 0 12px; color: #b0b0c8; }\n");
        w.write("table { border-collapse: separate; border-spacing: 3px; margin-bottom: 20px; }\n");
        w.write("td, th { width: 44px; height: 32px; text-align: center; font-size: 12px; border-radius: 4px; }\n");
        w.write("th { color: #8a8aa0; font-weight: 600; }\n");
        w.write("th.name, td.name { width: 200px; text-align: right; padding-right: 12px; color: #c8c8d8; }\n");
        w.write("a { color: #7dd3a0; text-decoration: none; }\n");
        w.write("</style></head><body>\n");
        w.write("<p><a href='index.html'>&larr; back</a></p>\n");
        w.write("<h1>Seed Distribution</h1>\n");
        writeHeatmapTable(w, "Eastern Conference", east, seedCounts, numSeasons, maxSeed);
        writeHeatmapTable(w, "Western Conference", west, seedCounts, numSeasons, maxSeed);
        w.write("</body></html>\n");
        w.close();
    }

    private static void writeHeatmapTable(FileWriter w, String title, List<Team> teams, Map<Team, int[]> seedCounts,
            int numSeasons, int maxSeed) throws IOException {
        w.write("<h2>" + title + "</h2>\n<table>\n");
        w.write("<tr><th class='name'>Team</th>");
        for (int seed = 1; seed <= maxSeed; seed++) {
            w.write("<th>" + seed + "</th>");
        }
        w.write("</tr>\n");
        for (Team team : teams) {
            w.write("<tr><td class='name'>" + team.getName() + "</td>");
            for (int seed = 1; seed <= maxSeed; seed++) {
                double pct = 100.0 * seedCounts.get(team)[seed] / numSeasons;
                int r = clamp((int) (140 - pct * 0.9));
                int g = clamp((int) (40 + pct * 1.9));
                int b = clamp((int) (180 - pct * 1.2));
                String bg = "rgb(" + r + "," + g + "," + b + ")";
                String label = pct >= 1.0 ? String.format("%.0f", pct) : "";
                w.write("<td style='background:" + bg + ";'>" + label + "</td>");
            }
            w.write("</tr>\n");
        }
        w.write("</table>\n");
    }

    public static void writeIndex(int season, String filename) throws IOException {
        FileWriter w = new FileWriter(filename);
        w.write("<!DOCTYPE html>\n<html><head><meta charset='utf-8'>\n");
        w.write("<title>NBA Simulator " + season + "</title>\n");
        w.write("<link href='https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;600;700&display=swap' rel='stylesheet'>\n");
        w.write("<style>\n");
        w.write("* { box-sizing: border-box; margin: 0; }\n");
        w.write("body { font-family: 'Space Grotesk', sans-serif; ");
        w.write("background: radial-gradient(circle at 30% 10%, #1a0b2e 0%, #0a0a0f 60%), ");
        w.write("linear-gradient(135deg, #0d1f14 0%, #0a0a0f 65%); ");
        w.write("background-blend-mode: screen; color: #f0f0f5; min-height: 100vh; padding: 80px 60px; }\n");
        w.write("h1 { font-size: 56px; font-weight: 700; letter-spacing: -2px; ");
        w.write("background: linear-gradient(90deg, #a855f7, #22e06b); ");
        w.write("-webkit-background-clip: text; -webkit-text-fill-color: transparent; margin-bottom: 12px; }\n");
        w.write(".sub { color: #8a8aa0; font-size: 16px; margin-bottom: 50px; }\n");
        w.write("a { display: block; color: #f0f0f5; font-size: 22px; font-weight: 600; ");
        w.write("margin: 16px 0; padding: 20px 24px; text-decoration: none; ");
        w.write("border: 1px solid #2a2a3a; border-radius: 12px; ");
        w.write("background: rgba(255,255,255,0.02); transition: all .2s; max-width: 480px; }\n");
        w.write("a:hover { border-color: #22e06b; background: rgba(34,224,107,0.08); }\n");
        w.write("</style></head><body>\n");
        w.write("<h1>NBA Season Simulator — " + season + "</h1>\n");
        w.write("<div class='sub'>Monte Carlo simulation - 10,000 seasons</div>\n");
        w.write("<a href='playoff-report.html'>&rarr; Playoff probabilities</a>\n");
        w.write("<a href='seed-heatmap.html'>&rarr; Seed distribution</a>\n");
        w.write("</body></html>\n");
        w.close();
    }

    private static int clamp(int v) {
        if (v < 0)
            return 0;
        if (v > 255)
            return 255;
        return v;
    }
}
