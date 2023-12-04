fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf(::getCardValue)
    }

    fun part2(input: List<String>): Long {
        val scratchCards = Array(input.size) {1L}
        input.forEachIndexed { index, s ->
            val points = getCardPoints(s)
            repeat(scratchCards[index].toInt()) {
                for (i in 1.. points) {
                    scratchCards[(index + i).coerceAtMost(input.lastIndex)]++
                }
            }
        }
        return scratchCards.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_1_test")
    check(part1(testInput) == 13L)

    val input = readInput("Day04")
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
}

fun getCardValue(s: String): Long {
    val cardPoints = getCardPoints(s)
    return if (cardPoints > 0) 1L shl (cardPoints - 1) else 0L
}

private fun getCardPoints(s: String): Int {
    val winning: Set<Int> = s
            .substringAfter(": ")
            .substringBefore(" |")
            .extractNumbers()
    val cardNumbers: Set<Int> = s
            .substringAfter("| ")
            .extractNumbers()
    val commonSize = winning.intersect(cardNumbers).size
    return commonSize
}

fun String.extractNumbers(): Set<Int> = split(' ')
        .filter { it.isNotEmpty() }
        .map(String::toInt)
        .toSet()
