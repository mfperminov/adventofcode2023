import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val rows = mutableListOf<MutableList<Char>>()
        var columnsToDuplicate = input.first().mapIndexed { index, c -> if (c == '.') index else null }.toSet()
        input.forEachIndexed { index, s ->
            rows.add(s.toCharArray().toMutableList())
            if (s.all { it == '.' }) {
                rows.add(s.toCharArray().toMutableList())
            }
            columnsToDuplicate = columnsToDuplicate.minus(s.mapIndexedNotNull { index, c -> if (c == '#') index else null }.toSet())
        }

        var additions = 0
        columnsToDuplicate.filterNotNull().forEach { index ->
            rows.forEach{ r ->
                if (index + additions >= r.size) {
                   r.add('.')
                } else {
                    r.add(index + additions, '.')
                }
            }
            additions++
        }
        var count = 0
        var galaxies = mutableListOf<Galaxy>()
        for (i in rows.indices) {
            for (j in rows[i].indices) {
                if (rows[i][j] == '#') {
                    galaxies.add(Galaxy(i, j))
                }
            }
        }
        var distances = 0
        for (i in galaxies.indices) {
            for (j in i until galaxies.size) {
                distances += galaxies[i].shortestDistance(galaxies[j])
            }
        }
        return distances
    }

    fun part2(input: List<String>): Long {
        val rows = mutableListOf<MutableList<Char>>()
        var columnsToDuplicate = input.first().mapIndexed { index, c -> if (c == '.') index else null }.toSet()
        input.forEachIndexed { index, s ->
            rows.add(s.toCharArray().toMutableList())
            if (s.all { it == '.' }) {
                repeat(1_000_000) {
                    rows.add(s.toCharArray().toMutableList())
                }
            }
            columnsToDuplicate = columnsToDuplicate.minus(s.mapIndexedNotNull { index, c -> if (c == '#') index else null }.toSet())
        }

        var additions = 0
        columnsToDuplicate.filterNotNull().forEach { index ->
            rows.forEach{ r ->
                if (index + additions >= r.size) {
                    repeat(1_000_000) {
                        r.add('.')
                    }
                } else {
                    repeat(1_000_000) {
                        r.add(index + additions, '.')
                    }
                }
            }
            additions++
        }
        var galaxies = mutableListOf<Galaxy>()
        for (i in rows.indices) {
            for (j in rows[i].indices) {
                if (rows[i][j] == '#') {
                    galaxies.add(Galaxy(i, j))
                }
            }
        }
        var distances = 0L
        for (i in galaxies.indices) {
            for (j in i until galaxies.size) {
                distances += galaxies[i].shortestDistance(galaxies[j])
            }
        }
        return distances
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

data class Galaxy(val row: Int, val column: Int) {
    fun shortestDistance(another: Galaxy): Int {
        return abs(row - another.row) + abs(column - another.column)
    }
}
