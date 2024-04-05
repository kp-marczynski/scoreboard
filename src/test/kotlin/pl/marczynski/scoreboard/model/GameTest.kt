package pl.marczynski.scoreboard.model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GameTest {

    @Test
    fun `game scores should be updated correctly`() {
        // given
        val newHomeScore = 1
        val newAwayScore = 2
        val game = Game("home", "away")
        assertEquals(0, game.homeScore)
        assertEquals(0, game.awayScore)

        // when
        val result = game.updateScores(newHomeScore, newAwayScore)

        // then
        assertEquals(newHomeScore, result.homeScore)
        assertEquals(newAwayScore, result.awayScore)
    }
}
