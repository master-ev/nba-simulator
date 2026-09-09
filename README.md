# NBA Season Simulator

Monte Carlo simulator for the NBA. Takes a season partway through and plays out
the rest ~10,000 times to get playoff odds, title odds, and likely seeds per team.
Generates HTML reports: probability bars and a seed-distribution heatmap.

## How it works

One simulated season isn't worth much, but run 10,000 and count how often each
team makes the playoffs and the result converges to a probability. Each remaining
game is decided by drawing a random number against the two teams' win probability;
do that for every game to get one season, repeat, and tally the outcomes.
Data (teams + every game) comes from the balldontlie API, cached to disk after
the first fetch. The playoff bracket includes the play-in tournament (seeds 7-10).

![Playoff probabilities](docs/playoff%20bars.png)
![Seed distribution - Eastern Conference](docs/Eastern%20Conference.png)
![Seed distribution - Western Conference](docs/Western%20Conference.png)

## Two models, and tuning them

Two ways to estimate a game's win probability:

- **Win rate** — strength is games won over games played.
- **Elo** — a rating that moves after each game, weighted by opponent strength.

I backtested both: cut a finished season at 70%, simulate the rest, and score the
predictions against what actually happened with Brier score (lower is better).

Out of the box, win rate beat Elo — the standard K=20 was too reactive for a
season-long question. So I tried to fix Elo:

- **Carry ratings from last season** — measured *worse* across every blend ratio.
  NBA teams change too much between seasons; last year's rating is mostly noise.
- **Home-court advantage** — also *worse* for playoff prediction. Over 82 games
  home and away roughly cancel; it just adds variance.
- **Tuning K** — this was the fix. Lower K means steadier ratings. K=5 made Elo
  beat win rate on both seasons.

```
K=5 vs win rate (Brier, lower is better)
Season 2024-25  elo 0.0454  win rate 0.0468
Season 2025-26  elo 0.0399  win rate 0.0405
```

The model wasn't weak, it was miscalibrated. Two "obvious" improvements made it
worse and the boring one — a smaller learning rate — made it work.

## Running it

```bash
mvn compile
mvn test
mvn exec:java -Dexec.mainClass="com.nba.App" -Dexec.args="--analysis reports --elo"
```

`--analysis` takes `playoffs`, `title`, `compare`, `reports`. `--season <year>`,
`--sims <n>`, `--elo` also work. Needs a balldontlie key in `BALLDONTLIE_API_KEY`.

## Stack

Java 21, Maven, Jackson (JSON), JUnit 5 (12 tests).

## Notes

The backtest was the interesting part, not the simulation. Printing plausible
numbers is easy; checking them against a finished season is what showed which
model actually worked — and that the fancy fixes I expected to help didn't.