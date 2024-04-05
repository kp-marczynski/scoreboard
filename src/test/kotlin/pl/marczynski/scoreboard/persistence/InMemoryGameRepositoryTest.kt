package pl.marczynski.scoreboard.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import pl.marczynski.scoreboard.model.Game

class InMemoryGameRepositoryTest {
    private val games = mutableSetOf<Game>()

    private val repository = InMemoryGameRepository(games)

    @AfterEach
    fun cleanup() {
        games.clear()
    }

    @Test
    fun `findAll should return games in correct order`() {
        // given
        val game1 = Game("Mexico", "Canada").updateScores(0, 5)
        val game2 = Game("Spain", "Brazil").updateScores(10, 2)
        val game3 = Game("Germany", "France").updateScores(2, 2)
        val game4 = Game("Uruguay", "Italy").updateScores(6, 6)
        val game5 = Game("Argentina", "Australia").updateScores(3, 1)

        listOf(game1, game2, game3, game4, game5).let { games.addAll(it) }

        // when
        val result = repository.findAll()

        // then
        assertThat(result).containsExactly(game4, game2, game1, game5, game3)
    }
    @Test
    fun `add should correctly add a game`() {
        // Given
        val homeTeam = "Home Team"
        val awayTeam = "Away Team"
        val game = Game(homeTeam, awayTeam)

        // When
        repository.add(game)

        // Then
        assertThat(games).containsExactly(game)
    }

    @Test
    fun `update should correctly update a game's score`() {
        // Given
        val homeTeam = "Home Team"
        val awayTeam = "Away Team"
        val newHomeScore = 2
        val newAwayScore = 3
        val game = Game(homeTeam, awayTeam)
        games.add(game)

        // When
        val updatedGame = game.updateScores( newHomeScore, newAwayScore)
        repository.update(updatedGame)

        // Then
        assertThat(games).hasSize(1)
        assertThat(games.first().homeScore).isEqualTo(newHomeScore)
        assertThat(games.first().awayScore).isEqualTo(newAwayScore)
    }

    @Test
    fun `removeByTeams should correctly remove a game`() {
        // Given
        val game1 = Game("Home Team 1", "Away Team 1")
        val game2 = Game("Home Team 2", "Away Team 2")
        games.addAll(listOf(game1, game2))

        // When
        repository.remove(game1)

        // Then
        assertThat(games).containsExactly(game2)
    }

    @Test
    fun `removeAll should clear all games`() {
        // Given
        val game1 = Game("Home Team 1", "Away Team 1")
        val game2 = Game("Home Team 2", "Away Team 2")
        games.addAll(listOf(game1, game2))

        // When
        repository.removeAll()

        // Then
        assertThat(games).isEmpty()
    }

    @Test
    fun `findByTeams should return the correct game`() {
        // Given
        val homeTeam = "Home Team"
        val awayTeam = "Away Team"
        val game = Game(homeTeam, awayTeam)
        games.add(game)

        // When
        val foundGame = repository.findByTeams(homeTeam, awayTeam)

        // Then
        assertThat(foundGame).isEqualTo(game)
    }
}
