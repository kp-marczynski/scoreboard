package pl.marczynski.scoreboard

import pl.marczynski.scoreboard.persistence.InMemoryGameRepository

object ScoreboardFactory {
    fun createScoreboard(): Scoreboard = Scoreboard(InMemoryGameRepository())
}

