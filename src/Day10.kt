fun main() {
    fun part1(input: List<String>): Int {
        var xS = 0
        var yS = 0
        val map = input.mapIndexed { x, s ->
            s.mapIndexed { y, ss ->
                Pipe(x, y, Tile.fromChar(ss)).also {
                    if (ss == 'S') {
                        xS = x
                        yS = y
                    }
                }
            }
        }

        val sBottom = if (xS + 1 < input.size) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS+1][yS]))
        } else {
            emptyList()
        }

        val sTop = if (xS - 1 >=0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS-1][yS]))
        } else {
            emptyList()
        }
        val sRight = if (yS + 1 < map[xS].size) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS+1]))
        } else {
            emptyList()
        }
        val sLeft = if (yS - 1 >= 0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS-1]))
        } else {
            emptyList()
        }
        return listOf(sTop, sRight, sBottom, sLeft).maxBy { it.size }.size / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    check(part1(testInput) == 8)
    check(part1(testInput2) == 4)
    part1(testInput).println()
    part1(testInput2).println()

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}

private enum class Direction {
    WEST,
    EAST,
    NORTH,
    SOUTH;
}

private enum class Tile(val ch: Char) {
    VERTICAL('|'),
    HORIZONTAL('-'),
    NORTH_EAST('L'),
    NORTH_WEST('J'),
    SOUTH_WEST('7'),
    SOUTH_EAST('F'),
    GROUND('.'),
    START('S');

    override fun toString(): String {
        return ch.toString()
    }

    fun rightConnection(): List<Tile> {
        return when (this) {
            VERTICAL -> emptyList()
            HORIZONTAL -> listOf(HORIZONTAL, NORTH_WEST, SOUTH_WEST, START)
            NORTH_EAST -> listOf(HORIZONTAL, NORTH_WEST, SOUTH_WEST, START)
            NORTH_WEST -> emptyList()
            SOUTH_WEST -> emptyList()
            SOUTH_EAST -> listOf(HORIZONTAL, NORTH_WEST, SOUTH_WEST, START)
            GROUND -> emptyList()
            START -> Tile.values().toList()
        }
    }

    fun leftConnection(): List<Tile> {
        return when (this) {
            VERTICAL -> emptyList()
            HORIZONTAL -> listOf(HORIZONTAL, NORTH_EAST, SOUTH_EAST, START)
            NORTH_EAST -> emptyList()
            NORTH_WEST -> listOf(HORIZONTAL, NORTH_EAST, SOUTH_EAST, START)
            SOUTH_WEST -> listOf(HORIZONTAL, NORTH_EAST, SOUTH_EAST, START)
            SOUTH_EAST -> emptyList()
            GROUND -> emptyList()
            START -> Tile.values().toList()
        }
    }

    fun topConnection(): List<Tile> {
        return when (this) {
            VERTICAL -> listOf(VERTICAL, SOUTH_WEST, SOUTH_EAST, START)
            HORIZONTAL -> emptyList()
            NORTH_EAST -> listOf(VERTICAL, SOUTH_WEST, SOUTH_EAST, START)
            NORTH_WEST -> listOf(VERTICAL, SOUTH_WEST, SOUTH_EAST, START)
            SOUTH_WEST -> emptyList()
            SOUTH_EAST -> emptyList()
            GROUND -> emptyList()
            START -> Tile.values().toList()
        }
    }

    fun bottomConnection(): List<Tile> {
        return when (this) {
            VERTICAL -> listOf(VERTICAL, NORTH_EAST, NORTH_WEST, START)
            HORIZONTAL -> emptyList()
            NORTH_EAST -> emptyList()
            NORTH_WEST -> emptyList()
            SOUTH_WEST -> listOf(VERTICAL, SOUTH_WEST, SOUTH_EAST, START)
            SOUTH_EAST -> listOf(VERTICAL, SOUTH_WEST, SOUTH_EAST, START)
            GROUND -> emptyList()
            START -> Tile.values().toList()
        }
    }

    companion object {
        fun fromChar(value: Char) = Tile.values().first { it.ch == value }
    }
}

private data class Pipe(val x: Int, val y: Int, val tile: Tile)


private fun nextPipe(map: List<List<Pipe>>, possibleLoop: MutableList<Pipe>): List<Pipe> {
    val last = possibleLoop.last()
    if (last.tile == Tile.START && possibleLoop.size != 2) return possibleLoop
    if (last.y - 1 >= 0 && last.tile.leftConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x][last.y - 1])|| map[last.x][last.y - 1].tile == Tile.START)) {
        val top = nextPipe(map, possibleLoop.apply {
            add(map[last.x][last.y - 1])
        })
        if (top.isNotEmpty() && top.last().tile == Tile.START) return top
    }
    if (last.y + 1 < map[last.x].size && last.tile.rightConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x][last.y + 1]) || map[last.x][last.y + 1].tile == Tile.START)) {
        val bottom = nextPipe(map, possibleLoop.apply {
            add(map[last.x][last.y + 1])
        })
        if (bottom.isNotEmpty() && bottom.last().tile == Tile.START) return bottom
    }
    if (last.x + 1 < map.size && last.tile.bottomConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x + 1][last.y]) || map[last.x + 1][last.y].tile == Tile.START)) {
        val right = nextPipe(map, possibleLoop.apply {
            add(map[last.x + 1][last.y])
        })
        if (right.isNotEmpty() && right.last().tile == Tile.START) return right
    }
    if (last.x - 1 >= 0 && last.tile.topConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x - 1][last.y]) || map[last.x + 1][last.y].tile == Tile.START)){
        val left = nextPipe(map, possibleLoop.apply {
            add(map[last.x - 1][last.y])
        })
        if (left.isNotEmpty() && left.last().tile == Tile.START) return left
    }
    return emptyList()
}
