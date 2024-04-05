package pl.marczynski.scoreboard.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GameTest {

    @Test
    fun `game scores should be updated correctly`() {
        // given
        val newHomeScore = 1
        val newAwayScore = 2
        val game = Game("home", "away")
        assertThat(game.homeScore).isEqualTo(0)
        assertThat(game.awayScore).isEqualTo(0)

        // when
        val result = game.updateScores(newHomeScore, newAwayScore)

        // then
        assertThat(result.homeScore).isEqualTo(newHomeScore)
        assertThat(result.awayScore).isEqualTo(newAwayScore)
    }

    @Test
    fun `total score should be calculated as sum of scores`() {
        // given
        val homeScore = 1
        val awayScore = 2
        val game = Game("home", "away").updateScores(homeScore, awayScore)

        // when
        val result = game.getTotalScore()

        // then
        assertThat(result).isEqualTo(3)
    }

    @Test
    fun `games should be sorted by total score descending, then by start time descending`() {
        // given
        val game1 = Game("Mexico", "Canada").updateScores(0, 5)
        val game2 = Game("Spain", "Brazil").updateScores(10, 2)
        val game3 = Game("Germany", "France").updateScores(2, 2)
        val game4 = Game("Uruguay", "Italy").updateScores(6, 6)
        val game5 = Game("Argentina", "Australia").updateScores(3, 1)

        val games = listOf(game1, game2, game3, game4, game5)

        // when
        val result = games.sorted()

        // then
        assertThat(result).containsExactly(game4, game2, game1, game5, game3)
    }
}
