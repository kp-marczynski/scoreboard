package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.exceptions.GameNotFoundException
import pl.marczynski.scoreboard.exceptions.InvalidScoreException
import pl.marczynski.scoreboard.exceptions.InvalidTeamException
import pl.marczynski.scoreboard.exceptions.TeamPlayException
import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

/**
 * Manages the scores and states of football games.
 *
 * @property gameRepository The repository for storing and retrieving game data.
 */
class Scoreboard(private val gameRepository: GameRepository) {
    /**
     * Starts a new game with the specified home and away teams.
     *
     * This method validates the game start request to ensure that the team names are valid
     * and that neither team is currently involved in another game.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @throws InvalidTeamException if the team names are invalid.
     * @throws TeamPlayException if either team is already involved in another game.
     */
    fun startGame(homeTeam: String, awayTeam: String) {
        validateGameStartRequest(homeTeam, awayTeam)
        gameRepository.add(Game(homeTeam, awayTeam))
    }

    /**
     * Updates the score for an ongoing game.
     *
     * Validates the score update request to ensure that both scores are non-negative,
     * and updates the game score in the repository.
     *
     * @param homeTeam The name of the home team in the game.
     * @param awayTeam The name of the away team in the game.
     * @param homeScore The new score of the home team.
     * @param awayScore The new score of the away team.
     * @throws InvalidScoreException if either score is negative.
     * @throws GameNotFoundException if no game exists between the specified teams.
     */
    fun updateScore(homeTeam: String, awayTeam: String, homeScore: Int, awayScore: Int) {
        validateUpdateScoreRequest(homeScore, awayScore)
        val game = fetchGameOrThrow(homeTeam, awayTeam)

        gameRepository.update(
            game.updateScores(homeScore, awayScore)
        )
    }

    /**
     * Finishes a game between the specified teams.
     *
     * Removes the game from the repository, effectively marking it as finished.
     *
     * @param homeTeam The name of the home team in the game.
     * @param awayTeam The name of the away team in the game.
     * @throws GameNotFoundException if no game exists between the specified teams.
     */
    fun finishGame(homeTeam: String, awayTeam: String) {
        val game = fetchGameOrThrow(homeTeam, awayTeam)
        gameRepository.remove(game)
    }

    /**
     * Retrieves a summary of all ongoing games.
     *
     * Returns a list of string representations of all games, sorted according to the
     * repository's sorting criteria.
     *
     * @return List of strings, each representing a game.
     */
    fun getSummary(): List<String> {
        return gameRepository.findAll().map { it.toString() }
    }

    private fun validateGameStartRequest(homeTeam: String, awayTeam: String) {
        when {
            homeTeam == awayTeam -> throw InvalidTeamException("A team cannot play against itself.")
            homeTeam.isBlank() || awayTeam.isBlank() -> throw InvalidTeamException("Team names must be non-empty and non-blank.")
            gameRepository.existsByTeam(homeTeam) -> throw TeamPlayException(homeTeam)
            gameRepository.existsByTeam(awayTeam) -> throw TeamPlayException(awayTeam)
        }
    }

    private fun validateUpdateScoreRequest(homeScore: Int, awayScore: Int) {
        if (homeScore < 0 || awayScore < 0) throw InvalidScoreException()
    }

    private fun fetchGameOrThrow(homeTeam: String, awayTeam: String): Game {
        return gameRepository.findByTeams(homeTeam, awayTeam) ?: throw GameNotFoundException()
    }
}
