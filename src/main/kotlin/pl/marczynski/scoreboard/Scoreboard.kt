package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.exceptions.GameNotFoundException
import pl.marczynski.scoreboard.exceptions.InvalidScoreException
import pl.marczynski.scoreboard.exceptions.InvalidTeamException
import pl.marczynski.scoreboard.exceptions.TeamPlayException
import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class Scoreboard(private val gameRepository: GameRepository) {
    fun startGame(homeTeam: String, awayTeam: String) {
        validateGameStartRequest(homeTeam, awayTeam)
        gameRepository.add(Game(homeTeam, awayTeam))
    }

    private fun validateGameStartRequest(homeTeam: String, awayTeam: String) {
        when {
            homeTeam == awayTeam -> throw InvalidTeamException("A team cannot play against itself.")
            homeTeam.isBlank() || awayTeam.isBlank() -> throw InvalidTeamException("Team names must be non-empty and non-blank.")
            gameRepository.existsByTeam(homeTeam) -> throw TeamPlayException(homeTeam)
            gameRepository.existsByTeam(awayTeam) -> throw TeamPlayException(awayTeam)
        }
    }

    fun updateScore(homeTeam: String, awayTeam: String, homeScore: Int, awayScore: Int) {
        validateUpdateScoreRequest(homeScore, awayScore)
        val game = fetchGameOrThrow(homeTeam, awayTeam)

        gameRepository.update(
            game.updateScores(homeScore, awayScore)
        )
    }

    private fun validateUpdateScoreRequest(homeScore: Int, awayScore: Int) {
        if (homeScore < 0 || awayScore < 0) throw InvalidScoreException()
    }

    private fun fetchGameOrThrow(homeTeam: String, awayTeam: String): Game {
        return gameRepository.findByTeams(homeTeam, awayTeam) ?: throw GameNotFoundException()
    }

    fun finishGame(homeTeam: String, awayTeam: String) {
        val game = fetchGameOrThrow(homeTeam, awayTeam)
        gameRepository.remove(game)
    }

    fun getSummary(): List<String> {
        return gameRepository.findAll().map { it.toString() }
    }
}
