package pl.marczynski.scoreboard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class ScoreboardTest {
    private val repository: GameRepository = mockk(relaxed = true)
    private val scoreboard = Scoreboard(repository)

    private val homeTeam = "home team"
    private val awayTeam = "away team"

    @Test
    fun `starting a game adds it to the scoreboard`() {
        // given
        every { repository.add(any()) } returns Unit

        // when
        scoreboard.startGame(homeTeam, awayTeam)

        // then
        verify { repository.add(match { it.homeTeam == homeTeam && it.awayTeam == awayTeam }) }
    }

    @Test
    fun `updating existing game score should update it on the scoreboard`() {
        // given
        val homeScore = 1
        val awayScore = 2
        every { repository.findByTeams(homeTeam, awayTeam) } returns Game(homeTeam, awayTeam)
        every { repository.update(any()) } returns Unit

        // when
        scoreboard.updateScore(homeTeam, awayTeam, homeScore, awayScore)

        // then
        verify { repository.update(match { it.homeTeam == homeTeam && it.awayTeam == awayTeam && it.homeScore == homeScore && it.awayScore == awayScore }) }
    }

    @Test
    fun `finishing existing game should remove it from the scoreboard`() {
        // given
        val game = Game(homeTeam, awayTeam)
        every { repository.findByTeams(homeTeam, awayTeam) } returns game
        every { repository.remove(any()) } returns Unit

        // when
        scoreboard.finishGame(homeTeam, awayTeam)

        // then
        verify { repository.remove(game) }
    }

    @Test
    fun `scoreboard summary should return existing games`() {
        // when
        scoreboard.getSummary()

        // then
        verify { repository.findAll() }
    }

    @Test
    fun `startGame throws IllegalArgumentException if home team is the same as away team`() {
        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.startGame(homeTeam, homeTeam)
        }

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception.message).contains("A team cannot play against itself.")
    }

    @Test
    fun `startGame throws IllegalArgumentException if home team name is blank`() {
        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.startGame("    ", homeTeam)
        }

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception.message).contains("Team names must be non-empty and non-blank.")
    }

    @Test
    fun `startGame throws IllegalArgumentException if away team name is blank`() {
        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.startGame(homeTeam, "")
        }

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception.message).contains("Team names must be non-empty and non-blank.")
    }

    @Test
    fun `startGame throws IllegalStateException if one of the teams is already playing`() {
        // given
        every { repository.existsByTeam(homeTeam) } returns true
        every { repository.existsByTeam(awayTeam) } returns false

        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.startGame(homeTeam, awayTeam)
        }

        // then
        assertThat(exception).isInstanceOf(IllegalStateException::class.java)
        assertThat(exception.message).contains("already involved")
    }

    @Test
    fun `startGame proceeds when neither team is playing`() {
        // given
        every { repository.existsByTeam(homeTeam) } returns false
        every { repository.existsByTeam(awayTeam) } returns false

        // when
        scoreboard.startGame(homeTeam, awayTeam)

        // then
        verify(exactly = 1) { repository.add(any()) }
    }

    @Test
    fun `updateScore throws IllegalArgumentException for negative home score`() {
        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.updateScore(homeTeam, awayTeam, -1, 0)
        }

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception.message).contains("Scores must be greater than or equal to 0.")
    }

    @Test
    fun `updateScore throws IllegalArgumentException for negative away score`() {
        // when
        val exception = assertThrows<RuntimeException> {
            scoreboard.updateScore(homeTeam, awayTeam, 0, -1)
        }

        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception.message).contains("Scores must be greater than or equal to 0.")
    }
}
