fun main() {
    fun part1(input: List<String>): Long {
        return input.map(::parseGame)
                .filter(::isGameValid)
                .sumOf { it.id }
    }

    fun part2(input: List<String>): Long {
        return input.map(::parseGame).map(::getPowerOfGame).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_1_test")
    check(part1(testInput) == 8L)

    val input = readInput("Day02_1")
    part1(input).println()
    check(part2(testInput) == 2286L)
    part2(input).println()
}

fun getPowerOfGame(game: Game): Long {
    val minRed = game.rounds.maxBy { it.red }.red
    val minGreen = game.rounds.maxBy { it.green }.green
    val minBlue = game.rounds.maxBy { it.blue }.blue
    return minRed * minGreen * minBlue
}

data class Game(val id: Long, val rounds: List<Round>)
data class Round(val red: Long, val green: Long, val blue: Long)

fun parseGame(s: String): Game {
    val id = s.substringBefore(':').substringAfter("Game ").toLong()
    val rounds = s.substringAfter(": ").split("; ").map { r ->
        var red = 0L
        var green = 0L
        var blue = 0L
        r.split(", ").map { balls ->
            val countToColor = balls.split(' ')
            when (countToColor[1]) {
                "red" -> red = countToColor[0].toLong()
                "green" -> green = countToColor[0].toLong()
                "blue" -> blue = countToColor[0].toLong()
            }
        }
        Round(red, green, blue)
    }
    return Game(id, rounds)
}

fun isGameValid(game: Game): Boolean {
    return game.rounds.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
}