import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val rows = mutableListOf<MutableList<Char>>()
        var columnsToDuplicate = input.first().mapIndexedNotNull { index, c -> if (c == '.') index else null }.toSet()
        var rowsToDuplicate = mutableSetOf<Int>()
        input.forEachIndexed { index, s ->
            rows.add(s.toCharArray().toMutableList())
            if (s.toCharArray().toMutableList().all { it == '.' }) {
                rowsToDuplicate.add(index)
            }
            columnsToDuplicate = columnsToDuplicate.minus(s.mapIndexedNotNull { index, c -> if (c == '#') index else null }.toSet())
        }
        val galaxies = mutableListOf<Galaxy>()
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
                distances += galaxies[i].shortestDistance(galaxies[j], rowsToDuplicate, columnsToDuplicate, k = 2)
            }
        }
        return distances
    }

    fun part2(input: List<String>): Long {
        val rows = mutableListOf<MutableList<Char>>()
        var columnsToDuplicate = input.first().mapIndexedNotNull { index, c -> if (c == '.') index else null }.toSet()
        var rowsToDuplicate = mutableSetOf<Int>()
        input.forEachIndexed { index, s ->
            rows.add(s.toCharArray().toMutableList())
            if (s.toCharArray().toMutableList().all { it == '.' }) {
                rowsToDuplicate.add(index)
            }
            columnsToDuplicate = columnsToDuplicate.minus(s.mapIndexedNotNull { index, c -> if (c == '#') index else null }.toSet())
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
                distances += galaxies[i].shortestDistance(galaxies[j], rowsToDuplicate, columnsToDuplicate, k = 1_000_000L)
            }
        }
        return distances
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    check(part1(input) == 9509330L)
    part2(input).println()
}

data class Galaxy(val row: Int, val column: Int) {

    fun shortestDistance(another: Galaxy, duplicatesRows: Set<Int>, duplicatesColumns: Set<Int>, k: Long = 1): Long {
        val dubRows = (Math.min(row, another.row)..Math.max(row, another.row)).count { duplicatesRows.contains(it) }
        val dubColumns = (Math.min(column, another.column)..Math.max(column, another.column)).count { duplicatesColumns.contains(it) }
        return abs(row - another.row) + abs(column - another.column) + (k-1) * (dubRows + dubColumns)
    }
}
