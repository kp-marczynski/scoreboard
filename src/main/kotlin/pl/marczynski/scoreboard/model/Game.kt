package pl.marczynski.scoreboard.model

import java.time.Instant

data class Game(val homeTeam: String, val awayTeam: String) : Comparable<Game> {
    private val startTime: Instant = Instant.now()
    var homeScore: Int = 0
        private set
    var awayScore: Int = 0
        private set

    fun updateScores(newHomeScore: Int, newAwayScore: Int): Game {
        this.homeScore = newHomeScore
        this.awayScore = newAwayScore
        return this
    }

    fun getTotalScore() = homeScore + awayScore
    override fun compareTo(other: Game): Int {
        return compareValuesBy(this, other,
            { -it.getTotalScore() },  // Descending total score
            { -it.startTime.nano } // Descending start time
        )
    }

    override fun toString(): String = "$homeTeam $homeScore - $awayTeam $awayScore"
}

