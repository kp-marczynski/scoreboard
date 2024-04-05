package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class Scoreboard(private val gameRepository: GameRepository) {

    fun startGame(homeTeam: String, awayTeam: String) {
        if (homeTeam == awayTeam) {
            throw IllegalArgumentException("A team cannot play against itself.")
        }
        if (homeTeam.isBlank() || awayTeam.isBlank()) {
            throw IllegalArgumentException("Team names must be non-empty and non-blank.")
        }
        if (listOf(homeTeam, awayTeam).any { gameRepository.existsByTeam(it) }) {
            throw IllegalStateException("One or both teams are already involved in another game.")
        }
        gameRepository.add(Game(homeTeam, awayTeam))
    }

    fun updateScore(homeTeam: String, awayTeam: String, homeScore: Int, awayScore: Int) {
        if (homeScore < 0 || awayScore < 0) {
            throw IllegalArgumentException("Scores must be greater than or equal to 0.")
        }
        val game = gameRepository.findByTeams(homeTeam, awayTeam) ?: throw NoSuchElementException("Game not found.")

        gameRepository.update(
            game.updateScores(homeScore, awayScore)
        )
    }

    fun finishGame(homeTeam: String, awayTeam: String) {
        val game = gameRepository.findByTeams(homeTeam, awayTeam) ?: throw NoSuchElementException("Game not found.")
        gameRepository.remove(game)
    }

    fun getSummary(): List<String> {
        return gameRepository.findAll().map { it.toString() }
    }
}
