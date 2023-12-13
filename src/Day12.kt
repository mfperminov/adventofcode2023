fun main() {
    fun part1(input: List<String>): Int {

        val a = input.map { s ->
            s.substringBefore(" ") to s.substringAfter(' ').extractIntNumbers(sep = ',')
        }
        val sum = a.sumOf { (s, condition) ->
            acceptedSplit(s, condition.toMutableList())
        }
        return sum
    }
    // сичтать количество точек

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    part1(testInput).println()

    val input = readInput("Day12")
    // part1(input).println()
    // part2(input).println()
}


private fun acceptedSplit(s: String, condition: MutableList<Int>): Int {
    if (s.isEmpty() && condition.isNotEmpty()) return 0
    if (condition.isEmpty()) {
        if (s.isEmpty() || s.all { it =='.' }) return 1
    }
    if (s.first() == '.') return acceptedSplit(s.substring(1), condition)
    if (s.first() == '?') return acceptedSplit("." + s.substring(1), condition) + acceptedSplit("#" + s.substring(1), condition)
    if (s.first() == '#') {
        if (condition.isNotEmpty() && s.length >= condition.first() && s.take(condition.first()).all { it != '.' }) {
            return acceptedSplit(s.substring((condition.first()+1).coerceAtMost(s.length)), condition.subList(1, condition.size))
        }
    }
    return 0
}
