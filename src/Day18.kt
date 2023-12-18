import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val inputPoints = input.map(InputPoint::fromString)
        val points = mutableListOf<Point>()
        var init = Point(inputPoints.first(), 0, 0)
        points.add(init)
        inputPoints.forEach { p ->
            init = init.copy(targetInput = p)
            repeat(p.count) {
                init = when (p.dir) {
                    Dir.U -> init.copy(y = init.y - 1)
                    Dir.D -> init.copy(y = init.y + 1)
                    Dir.R -> init.copy(x = init.x + 1)
                    Dir.L -> init.copy(x = init.x - 1)
                }
                points.add(init)
            }
        }
        val map = points.associateBy { it.x to it.y }
        val maxX = points.maxBy { it.x }.x
        val maxY = points.maxBy { it.y }.y
        val minX = points.minBy { it.x }.x
        val minY = points.minBy { it.y }.y
        val arr = Array(maxY - minY + 1) { Array(maxX - minX + 1) { '.' } }
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (map.containsKey(x to y)) {
                    arr[y + abs(minY)][x + abs(minX)] = '#'
                }
            }
        }
        var sum = 0L
        arr.forEach {
            var state = State.out
            it.forEach { c ->
                if (c=='#') {
                    state = when(state) {
                        State.out -> State.enter_border
                        State.ins -> State.exit_border
                        State.enter_border -> State.enter_border
                        State.exit_border -> State.exit_border
                    }
                }
                if (c== '.') {
                    state = when(state) {
                        State.out -> State.out
                        State.ins -> State.ins
                        State.enter_border -> State.ins
                        State.exit_border -> State.out
                    }
                }
                if (state!=State.out || c=='#') {
                    sum++
                }

            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)

    val input = readInput("Day18")
    part1(input).println()
//    part2(input).println()
}

private enum class State {
    out,
    ins,
    enter_border,
    exit_border;
}

private data class InputPoint(val dir: Dir, val count: Int, val hex: String) {

    companion object {
        fun fromString(s: String): InputPoint {
            val (rawDir, count, hex) = s.split(" ")
            return InputPoint(Dir.valueOf(rawDir), count.toInt(), hex)
        }
    }
}

private enum class Dir {
    U,
    D,
    R,
    L
}

private data class Point(val targetInput: InputPoint, val x: Int, val y: Int)
