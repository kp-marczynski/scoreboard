package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class Scoreboard(private val gameRepository: GameRepository) {

    fun startGame(homeTeam: String, awayTeam: String) {
        val game = Game(homeTeam, awayTeam)
        gameRepository.add(game)
    }

    fun updateScore(homeTeam: String, awayTeam: String, homeScore: Int, awayScore: Int) {
        gameRepository.findByTeams(homeTeam, awayTeam)
            ?.updateScores(homeScore, awayScore)
            ?.let { gameRepository.update(it) }
    }

    fun finishGame(homeTeam: String, awayTeam: String) {
        gameRepository.findByTeams(homeTeam, awayTeam)
            ?.let { gameRepository.remove(it) }
    }

    fun getSummary(): List<String> {
        return gameRepository.findAll().map { it.toString() }
    }
}
