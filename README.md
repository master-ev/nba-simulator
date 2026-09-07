# NBA Season Simulator

Monte Carlo simulator for the NBA. Takes a season partway through and plays out the rest ~10,000 times to get playoff odds, title odds, and likely seeds per team.

# How it works

One simulated season isn't worth much, but run 10,000 and count how often each team makes the playoffs and the result converges to a probability. Each remaining game is decided by drawing a random number against the two teams' win probability; do that for every game to get one season, repeat, and tally the outcomes.
Data (teams + every game) comes from the balldontlie API, cached to disk after the first fetch.

## Two models

Two ways to estimate a game's win probability:

- **Win rate** - strength is just games won over games played.
- **Elo** - a rating that moves after each game, weighted by opponent strength.

To compare them I backtested: cut a finished season at 70%, simulate the rest, and score the predictions against what actually happened with Brier score (mean squared error, lower is better).

`Season 2024-2025 win rate 0.0463 elo 0.0560`

`Season 2025-2026 win rate 0.0411 elo 0.0494`

Win rate came out ahead both times. Elo starts cold mid-season and leans on recent form, but making the playoffs is a cumulative-record question, so the simpler model does better here. Not what I expected going in.

## Stack

Java 21, Maven, Jackson for JSON, JUnit 5. `mvn compile` / `mvn test`. Needs a balldontlie key in `BALLDONTLIE_API_KEY`.

## Notes

The backtest was the interesting part, not the simulation. Getting plausible-looking numbers is easy; checking them against a finished season is what showed the "better" model was actually the worse one.