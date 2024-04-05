# Scoreboard

## Assumptions
* Goal is to achieve balance between simplicity and proper separation of responsibilities.
* Repository layer will use simple in-memory storage and will directly operate on domain model.
* Scoreboard will hide all implementation details. Api will operate on team names instead on domain model, and summary will produce list of string representations.
* Instead of operating on arbitrary game id, homeTeam & awayTeam will be used for equality check.
* To not allow of uncontrolled changes of game scores, defensive mechanism in game model will be used.
* Game model toString() method will produce game summary that will be used in scoreboard.
