package pl.marczynski.scoreboard.persistence

import pl.marczynski.scoreboard.model.Game

class InMemoryGameRepository(private val games: MutableCollection<Game> = mutableListOf()) : GameRepository {
    override fun add(game: Game) {
        games.add(game)
    }

    override fun update(game: Game) {
        games.removeIf { it.homeTeam == game.homeTeam && it.awayTeam == game.awayTeam }
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

    override fun existsByTeam(team: String): Boolean {
        return games.any { it.homeTeam == team || it.awayTeam == team }
    }

    override fun findAll(): Collection<Game> {
        return games.sorted()
    }
}
