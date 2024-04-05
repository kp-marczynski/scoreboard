package pl.marczynski.scoreboard.model

data class Game(val homeTeam: String, val awayTeam: String) {
    var homeScore: Int = 0
        private set
    var awayScore: Int = 0
        private set

    fun updateScores(newHomeScore: Int, newAwayScore: Int): Game {
        this.homeScore = newHomeScore
        this.awayScore = newAwayScore
        return this
    }
}

