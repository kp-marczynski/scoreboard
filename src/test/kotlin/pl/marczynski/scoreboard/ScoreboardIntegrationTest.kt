package pl.marczynski.scoreboard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.InMemoryGameRepository

class ScoreboardIntegrationTest {
    private val repository = InMemoryGameRepository()
    private val scoreboard = Scoreboard(repository)

    private val homeTeam = "home team"
    private val awayTeam = "away team"

    @AfterEach
    fun setup() {
        repository.removeAll()
    }

    @Test
    fun `starting a game adds it to the scoreboard`() {
        // when
        scoreboard.startGame(homeTeam, awayTeam)

        // then
        val result = repository.findAll()
        assertThat(result).hasSize(1)
        val game = result.first()
        assertThat(game.homeTeam).isEqualTo(homeTeam)
        assertThat(game.awayTeam).isEqualTo(awayTeam)
        assertThat(game.homeScore).isEqualTo(0)
        assertThat(game.awayScore).isEqualTo(0)
    }

    @Test
    fun `updating existing game score should update it on the scoreboard`() {
        // given
        val homeScore = 1
        val awayScore = 2
        repository.add(Game(homeTeam, awayTeam))

        // when
        scoreboard.updateScore(homeTeam, awayTeam, homeScore, awayScore)

        // then
        val result = repository.findAll()
        assertThat(result).hasSize(1)
        val game = result.first()
        assertThat(game.homeTeam).isEqualTo(homeTeam)
        assertThat(game.awayTeam).isEqualTo(awayTeam)
        assertThat(game.homeScore).isEqualTo(homeScore)
        assertThat(game.awayScore).isEqualTo(awayScore)
    }

    @Test
    fun `finishing existing game should remove it from the scoreboard`() {
        // given
        repository.add(Game(homeTeam, awayTeam))

        // when
        scoreboard.finishGame(homeTeam, awayTeam)

        // then
        val result = repository.findAll()
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `scoreboard summary should return existing games`() {
        // given
        val game1 = Game("Mexico", "Canada").updateScores(0, 5)
        val game2 = Game("Spain", "Brazil").updateScores(10, 2)
        val game3 = Game("Germany", "France").updateScores(2, 2)
        val game4 = Game("Uruguay", "Italy").updateScores(6, 6)
        val game5 = Game("Argentina", "Australia").updateScores(3, 1)
        listOf(game1, game2, game3, game4, game5).map { repository.add(it) }

        // when
        val result = scoreboard.getSummary()

        // then
        assertThat(result).containsExactly(
            "Uruguay 6 - Italy 6",
            "Spain 10 - Brazil 2",
            "Mexico 0 - Canada 5",
            "Argentina 3 - Australia 1",
            "Germany 2 - France 2"
        )
    }
}
