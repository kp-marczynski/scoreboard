package pl.marczynski.scoreboard.exceptions

class TeamPlayException(teamName: String) : IllegalStateException("$teamName is already involved in another game.")
class InvalidTeamException(message: String) : IllegalArgumentException(message)
class InvalidScoreException : IllegalArgumentException("Scores must be greater than or equal to 0.")
class GameNotFoundException : NoSuchElementException("Game not found.")
