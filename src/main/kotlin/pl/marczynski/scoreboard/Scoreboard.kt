package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class Scoreboard(private val gameRepository: GameRepository) {

    fun startGame(homeTeam: String, awayTeam: String): Game {
        val game = Game(homeTeam, awayTeam)
        gameRepository.add(game)
        return game
    }

    fun updateScore(homeTeam: String, awayTeam: String, homeScore: Int, awayScore: Int) {
        gameRepository.findByTeams(homeTeam, awayTeam)
            ?.updateScores(homeScore, awayScore)
            ?.let { gameRepository.update(it) }
    }

    fun finishGame(homeTeam: String, awayTeam: String) {
        gameRepository.removeByTeams(homeTeam, awayTeam)
    }

    fun getSummary(): List<Game> {
        return gameRepository.findAll()
    }
}
