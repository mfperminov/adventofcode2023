val digitsSpelled = mapOf(
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
        return input.map(::normalizeInput).sumOf(::extractNumber)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(partOne(testInput) == 142)

    val input = readInput("Day01")
    partOne(input).println()

    val testInput_2 = readInput("Day01_2_test")
    println(testInput_2)
    println(testInput_2.map(::normalizeInput))
    check(partTwo(testInput_2) == 281)
    partTwo(input).println()
}

fun extractNumber(s: String): Int {
    val firstDigit = s.first { it.isDigit() }.digitToInt()
    val lastDigit = s.last { it.isDigit() }.digitToInt()
    return firstDigit * 10 + lastDigit
}

fun normalizeInput(s: String): String {
    var result = s
    val firstEntry: Map.Entry<String, Int> = digitsSpelled.minBy {
        val index = s.indexOf(it.key)
        if (index == -1) {
            Int.MAX_VALUE
        } else {
            index
        }
    }
    result = result.replace(firstEntry.key, "${firstEntry.value}")
    val lastEntry: Map.Entry<String, Int> = digitsSpelled.maxBy {
       result.indexOf(it.key)
    }
    result = result.replace(lastEntry.key, "${lastEntry.value}")
    return result
}