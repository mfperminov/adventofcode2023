fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds: ").extractNumbers()
        val maps: List<List<MapEntry>> = input.subList(1, input.size).splitToMaps()
        return seeds.minOf {
            var initial: Long = it
            maps.forEach { currentMap ->
                val map = currentMap.firstOrNull { it.calculateDst(initial) != null }
                if (map != null) {
                    initial = map.calculateDst(initial)!!
                }
            }
            initial
        }
    }

    fun part2(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds: ").extractNumbers().chunked(2)
        val maps: List<List<MapEntry>> = input.subList(1, input.size).splitToMaps()
        return seeds.minOf {
            val (start, range) = it
            (start until start + range).minOf {
                var initial: Long = it
                maps.forEach { currentMap ->
                    val map = currentMap.firstOrNull { it.calculateDst(initial) != null }
                    if (map != null) {
                        initial = map.calculateDst(initial)!!
                    }
                }
                initial
            }.also { println("finish map $start $range") }

        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_1_test")
    part1(testInput).println()
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.splitToMaps(): List<List<MapEntry>> {
    return extractData().map { rawMap ->
        rawMap.filter { it.isNotEmpty() }.map { it.extractNumbers() }.toGardenMap()
    }
}

private fun List<List<Long>>.toGardenMap(): List<MapEntry> {
    return map {
        val (dstStart, srcStart, rangeLength) = it
        MapEntry(dstStart, srcStart, rangeLength)
    }
}

data class MapEntry(val dstStart: Long, val srcStart: Long, val rangeLength: Long) {
    fun calculateDst(src: Long): Long? {
        return if (src in srcStart until srcStart + rangeLength) {
            dstStart + (src - srcStart)
        } else {
            null
        }
    }
}

private fun List<String>.extractData(): List<List<String>> {
    val result = mutableListOf<MutableList<String>>()
    var temp = mutableListOf<String>()
    for (item in this) {
        if (item.contains("map")) {
            continue
        }
        if (item == "") {
            result.add(temp)
            temp = mutableListOf()
        } else {
            temp.add(item)
        }
    }
    result.add(temp)
    return result
}

private fun String.extractNumbers(): List<Long> = split(' ')
        .filter { it.isNotBlank() }
        .map(String::toLong)
        .toList()
