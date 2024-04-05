# Scoreboard
Library for managing Live Football World Cup Scoreboard, that shows all the ongoing matches and their
scores.
The scoreboard supports the following operations:
1. Start a new game, assuming initial score 0 – 0 and adding it the scoreboard.
   This should capture following parameters:
   * Home team
   * Away team
2. Update score. This should receive a pair of absolute scores: home team score and away
   team score.
3. Finish game currently in progress. This removes a match from the scoreboard.
4. Get a summary of games in progress ordered by their total score. The games with the same
   total score will be returned ordered by the most recently started match in the scoreboard.

## Prerequisites
* Java 17

## How to use
### build & tests
```bash
./gradlew build
```
### package as jar
```bash
./gradlew jar
```
### use in your project
Load jar file to your project from [releases](https://github.com/kp-marczynski/scoreboard/releases).

Then initiate Scoreboard with `ScoreboardFactory.createScoreboard()`

## Assumptions

* Goal is to achieve balance between simplicity and proper separation of responsibilities.
* Repository layer will use simple in-memory storage and will directly operate on domain model.
* Scoreboard class will hide all implementation details. Api will operate on team names instead on domain model, 
  and summary will produce list of string representations.
* Instead of operating on arbitrary game id, homeTeam & awayTeam will be used for game identification.
* To not allow of uncontrolled changes of game scores, defensive mechanism in game model will be used.
* Game model toString() method will produce game summary that will be used in scoreboard.
* Only ongoing games are stored. Finished games are removed from database.
* Functional assumptions:
  * score must be >= 0
  * team name cannot be blank
  * team can't play with itself
  * team can only have 1 active game
