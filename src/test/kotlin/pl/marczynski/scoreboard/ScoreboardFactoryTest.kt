package pl.marczynski.scoreboard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ScoreboardFactoryTest {

    @Test
    fun `factory should create scoreboard instance with default settings`() {
        // when
        val result = ScoreboardFactory.createScoreboard()

        // then
        assertThat(result).isInstanceOf(Scoreboard::class.java)
        assertThat(result)
    }
}
