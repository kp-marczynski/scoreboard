package pl.marczynski.scoreboard.persistence

import pl.marczynski.scoreboard.model.Game

class InMemoryGameRepository(private val games: MutableSet<Game> = mutableSetOf()) : GameRepository {
    override fun add(game: Game) {
        games.add(game)
    }

    override fun update(game: Game) {
        games.add(game)
    }

    override fun remove(game: Game) {
        games.remove(game)
    }

    override fun removeAll() {
        games.clear()
    }

    override fun findByTeams(homeTeam: String, awayTeam: String): Game? {
        return games.find { it.homeTeam == homeTeam && it.awayTeam == awayTeam }
    }

    override fun findAll(): List<Game> {
        return games.sorted()
    }
}
