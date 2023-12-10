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
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS + 1][yS]))
        } else {
            emptyList()
        }

        val sTop = if (xS - 1 >= 0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS - 1][yS]))
        } else {
            emptyList()
        }
        val sRight = if (yS + 1 < map[xS].size) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS + 1]))
        } else {
            emptyList()
        }
        val sLeft = if (yS - 1 >= 0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS - 1]))
        } else {
            emptyList()
        }
        return listOf(sTop, sRight, sBottom, sLeft).maxBy { it.size }.size / 2
    }

    fun part2(input: List<String>): Int {
        var xS = 0
        var yS = 0
        var map = input.mapIndexed { x, s ->
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
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS + 1][yS]))
        } else {
            emptyList()
        }

        val sTop = if (xS - 1 >= 0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS - 1][yS]))
        } else {
            emptyList()
        }
        val sRight = if (yS + 1 < map[xS].size) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS + 1]))
        } else {
            emptyList()
        }
        val sLeft = if (yS - 1 >= 0) {
            nextPipe(map, mutableListOf(Pipe(xS, yS, Tile.START), map[xS][yS - 1]))
        } else {
            emptyList()
        }
        val path = listOf(sTop, sRight, sBottom, sLeft).maxBy { it.size }
        // https://en.wikipedia.org/wiki/Nonzero-rule
        val sRotate = if (path[1].x > path[0].x) {
            Rotate.BOTTOM
        } else {
            Rotate.TOP
        }
        map.map { l ->
            l.map {
                if (it.tile == Tile.START) {
                    it.rotate = sRotate
                    it
                } else {
                    it
                }
            }
        }
        for (i in 0 until path.lastIndex) {
            if (path[i].tile == Tile.SOUTH_EAST) {
                if (path[i + 1].y > path[i].y) {
                    path[i].rotate = Rotate.TOP
                }
                if (path[i + 1].x > path[i].x) {
                    path[i].rotate = Rotate.BOTTOM
                }
            }
            if (path[i].tile == Tile.SOUTH_WEST) {
                if (path[i + 1].x > path[i].x) {
                    path[i].rotate = Rotate.BOTTOM
                }
                if (path[i + 1].y < path[i].y) {
                    path[i].rotate = Rotate.TOP
                }
            }
            if (path[i].tile == Tile.VERTICAL) {
                if (path[i + 1].x > path[i].x) {
                    path[i].rotate = Rotate.BOTTOM
                } else {
                    path[i].rotate = Rotate.TOP
                }
            }
        }
        var count = 0
        map.forEach { l ->
            var crossCounter = 0
            var firstRotation: Rotate? = null
            l.forEach {
                if (it.rotate != null && firstRotation == null) {
                    firstRotation = it.rotate
                }
                if (it.rotate == Rotate.TOP) {
                    if (firstRotation == Rotate.TOP) crossCounter++ else crossCounter--
                } else if (it.rotate == Rotate.BOTTOM) {
                    if (firstRotation == Rotate.TOP) crossCounter-- else crossCounter++
                }
                if (!path.contains(it) && crossCounter > 0) count++
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
    val testInput5 = readInput("Day10_test5")
    check(part1(testInput) == 8)
    check(part1(testInput2) == 4)
    val input = readInput("Day10")
    part1(input).println()
    check(part2(testInput3) == 4)
    check(part2(testInput4) == 8)
    check(part2(testInput5) == 10)
    part2(input).println()
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

enum class Rotate {
    TOP,
    BOTTOM
}

private data class Pipe(val x: Int, val y: Int, val tile: Tile, var rotate: Rotate? = null)


private fun nextPipe(map: List<List<Pipe>>, possibleLoop: MutableList<Pipe>): List<Pipe> {
    val last = possibleLoop.last()
    if (last.tile == Tile.START && possibleLoop.size != 2) return possibleLoop
    if (last.y - 1 >= 0 && last.tile.leftConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x][last.y - 1]) || map[last.x][last.y - 1].tile == Tile.START)) {
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
    if (last.x - 1 >= 0 && last.tile.topConnection().isNotEmpty() && (!possibleLoop.contains(map[last.x - 1][last.y]) || map[last.x - 1][last.y].tile == Tile.START)) {
        val left = nextPipe(map, possibleLoop.apply {
            add(map[last.x - 1][last.y])
        })
        if (left.isNotEmpty() && left.last().tile == Tile.START) return left
    }
    return emptyList()
}
