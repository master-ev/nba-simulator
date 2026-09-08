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
        w.write("<style>\n");
        w.write("body { font-family: sans-serif; background: #111; color: #eee; padding: 30px; }\n");
        w.write("h1 { font-weight: 600; }\n");
        w.write(".row { display: flex; align-items: center; margin: 4px 0; }\n");
        w.write(".name { width: 200px; text-align: right; padding-right: 12px; font-size: 14px; }\n");
        w.write(".bar { height: 20px; border-radius: 3px; }\n");
        w.write(".pct { padding-left: 8px; font-size: 13px; color: #aaa; }\n");
        w.write("</style></head><body>\n");
        w.write("<h1>Playoff Probabilities</h1>\n");
        for (Team team : teams) {
            double prob = 100.0 * counts.get(team) / numSeasons;
            // low to high
            int red = (int) (255 * (1 - prob / 100.0));
            int green = (int) (255 * (prob / 100.0));
            String color = "rgb(" + red + "," + green + ",80)";
            w.write("<div class='row'>\n");
            w.write("   <div class='name'>" + team.getName() + "</div>\n");
            w.write("   <div class='bar' style='width:" + (prob * 4) + "px; background:" + color + ";'></div>\n");
            w.write("   <div class='pct'>" + String.format("%.1f%%", prob) + "</div>\n");
            w.write("</div>\n");
        }
        w.write("</body></html>\n");
        w.close();
    }
}
