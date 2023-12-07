fun main() {
    fun part1(input: List<String>): Int {
        val validCoordinates = mutableListOf<Pair<Int, Int>>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (!input[i][j].isDigit() && input[i][j] != '.') {
                    for (k in i - 1..i + 1) {
                        for (l in j - 1..j + 1) {
                            validCoordinates.add(k to l)
                        }
                    }
                }
            }
        }
        var sum = 0
        for (i in input.indices) {
            var nextNumber = ""
            var isValid = false
            for (j in input[i].indices) {
                if (input[i][j].isDigit()) {
                    nextNumber += input[i][j]
                    if (!isValid) {
                        isValid = validCoordinates.contains(i to j)
                    }
                    if (j == input[i].lastIndex && isValid) {
                        sum += nextNumber.toInt()
                        nextNumber = ""
                        isValid = false
                    }
                } else {
                    if (nextNumber.isNotEmpty() && isValid) {
                        sum += nextNumber.toInt()
                    }
                    nextNumber = ""
                    isValid = false
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val validCoordinates = mutableMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == '*') {
                    for (k in i - 1..i + 1) {
                        for (l in j - 1..j + 1) {
                            validCoordinates.computeIfAbsent(i to j) { mutableListOf<Pair<Int, Int>>() }
                            validCoordinates[i to j]!!.add(k to l)
                        }
                    }
                }
            }
        }
        var sum = 0
        val validNumbers = mutableMapOf<Pair<Int, Int>, MutableSet<Int>>()
        for (i in input.indices) {
            var nextNumber = ""
            var isValid = false
            val gearCoordinates = mutableListOf<Pair<Int, Int>>()
            for (j in input[i].indices) {
                if (input[i][j].isDigit()) {
                    nextNumber += input[i][j]
                    validCoordinates.forEach { (gearCoordinate, acrossCoordinates) ->
                        val contains = acrossCoordinates.contains(i to j)
                        if (contains) {
                            gearCoordinates.add(gearCoordinate)
                        }
                        isValid = true
                    }
                    if (j == input[i].lastIndex && isValid) {
                        gearCoordinates.forEach {
                            validNumbers.computeIfAbsent(it) { mutableSetOf<Int>() }
                            validNumbers[it]!!.add(nextNumber.toInt())
                        }
                        nextNumber = ""
                        gearCoordinates.clear()
                        isValid = false
                    }
                } else {
                    if (nextNumber.isNotEmpty() && isValid) {
                        gearCoordinates.forEach {
                            validNumbers.computeIfAbsent(it) { mutableSetOf<Int>() }
                            validNumbers[it]!!.add(nextNumber.toInt())
                        }
                    }
                    nextNumber = ""
                    gearCoordinates.clear()
                    isValid = false
                }
            }
        }
        sum = validNumbers.filter { entry -> entry.value.size == 2 }.values.map { it.fold(1) {acc: Int, i: Int -> acc *i } }.sum()
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")

    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}


