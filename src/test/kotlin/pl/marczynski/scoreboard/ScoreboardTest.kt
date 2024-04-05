package pl.marczynski.scoreboard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import pl.marczynski.scoreboard.model.Game
import pl.marczynski.scoreboard.persistence.GameRepository

class ScoreboardTest {
    private val repository: GameRepository = mockk(relaxed = true)
    private val scoreboard = Scoreboard(repository)

    @Test
    fun `starting a game adds it to the scoreboard`() {
        // given
        val homeTeam = "a"
        val awayTeam = "b"
        every { repository.add(any()) } returns Unit

        // when
        scoreboard.startGame(homeTeam, awayTeam)

        // then
        verify { repository.add(match { it.homeTeam == homeTeam && it.awayTeam == awayTeam }) }
    }

    @Test
    fun `updating existing game score should update it on the scoreboard`() {
        // given
        val homeTeam = "a"
        val awayTeam = "b"
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
        val homeTeam = "a"
        val awayTeam = "b"
        every { repository.removeByTeams(any(), any()) } returns Unit

        // when
        scoreboard.finishGame(homeTeam, awayTeam)

        // then
        verify { repository.removeByTeams(homeTeam, awayTeam) }
    }

    @Test
    fun `scoreboard summary should return existing games`() {
        // when
        scoreboard.getSummary()

        // then
        verify { repository.findAll() }
    }
}
