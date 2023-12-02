val digitsSpelled = mapOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
)

fun main() {
    fun partOne(input: List<String>): Int {
        return input.sumOf(::extractNumber)
    }

    fun partTwo(input: List<String>): Int {
        return input.sumOf{
            val result = extractDigits(it)
            result.first().digitToInt()* 10 + result.last().digitToInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(partOne(testInput) == 142)

    val input = readInput("Day01")
    partOne(input).println()

    val testInput_2 = readInput("Day01_2_test")
    check(partTwo(testInput_2) == 281)
    partTwo(input).println()
}

fun extractNumber(s: String): Int {
    val firstDigit = s.first { it.isDigit() }.digitToInt()
    val lastDigit = s.last { it.isDigit() }.digitToInt()
    return firstDigit * 10 + lastDigit
}

fun extractDigits(s: String): String {
    return buildString {
        for (i in s.indices) {
            if (s[i].isDigit()) append(s[i])
            val ss = s.substring(i)
            digitsSpelled.forEach { (t, u) ->
                if (ss.startsWith(t)) {
                    append(u)
                }
            }
        }
    }
}
