fun main() {
    fun part1(input: List<String>): Int {
        val seeds = input[0].substringAfter("seeds: ").extractNumbers()
        val maps: List<Map<Int, Int>> = input.subList(1, input.size).splitToMaps()
        return seeds.minOf {
            var initial = it
            maps.forEach { currentMap ->
                if (currentMap.contains(initial)) {
                    initial = currentMap[initial]!!
                }
            }
            initial
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_1_test")
    part1(testInput).println()
    // check(part1(testInput) == 1)

    // val input = readInput("Day05")
    // part1(input).println()
    // part2(input).println()
}

private fun List<String>.splitToMaps(): List<Map<Int, Int>> {
    return extractData().map { rawMap ->
        rawMap.map { it.extractNumbers().toGardenMap() }
    }
}

private fun List<Int>.toGardenMap(): Map<Int, Int> {
    val (dstStart, srcStart, rangeLength) = this
    return buildMap {
        for (i in 0 until rangeLength) {
            put(srcStart + i, dstStart + i)
        }
    }

}

private fun List<String>.extractData(): List<List<String>> {
    val result = mutableListOf<MutableList<String>>()
    val temp = mutableListOf<String>()
    for (item in this) {
        if (item.contains("map")) {
            continue
        }
        if (item == " ") {
            result.add(temp)
            temp.clear()
        } else {
            temp.add(item)
        }
    }
    result.add(temp)
    return result
}

private fun String.extractNumbers(): List<Int> = split(' ')
    .filter { it.isNotEmpty() }
    .map(String::toInt)
    .toList()
