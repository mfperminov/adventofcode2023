fun main() {

    fun part1(input: List<String>): Int {
        val times = input[0].substringAfter("Time:      ").extractIntNumbers()
        val distances = input[1].substringAfter("Distance:  ").extractIntNumbers()
        return times.zip(distances).map { (time, distance) ->
            calculateIntWinningVariants(time, distance)
        }
            .fold(1) { acc, i ->
                acc * i
            }
    }

    fun part2(input: List<String>): Long {
        val times = input[0].substringAfter("Time:      ").extractLongNumbers().joinToString(separator = "").toLong()
        val distances = input[1].substringAfter("Distance:  ").extractLongNumbers().joinToString(separator = "").toLong()
        return calculateWinningVariants(times, distances)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_1_test")
    check(part1(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
}

private fun calculateIntWinningVariants(time: Int, distance: Int): Int {
    var result = 0
    for (i in 0..time) {
        val distanceForTime = i /*speed*/ * (time - i) /*time*/
        if (distanceForTime > distance) {
            result++
        }
    }
    return result
}

private fun calculateWinningVariants(time: Long, distance: Long): Long {
    var result = 0L
    for (i in 0..time) {
        val distanceForTime = i /*speed*/ * (time - i) /*time*/
        if (distanceForTime > distance) {
            result++
        }
    }
    return result
}