package pl.marczynski.scoreboard.persistence

import pl.marczynski.scoreboard.model.Game

interface GameRepository {
    fun add(game: Game)
    fun update(game: Game)
    fun remove(game: Game)
    fun removeAll()
    fun findByTeams(homeTeam: String, awayTeam: String): Game?
    fun findAll(): List<Game>
}
