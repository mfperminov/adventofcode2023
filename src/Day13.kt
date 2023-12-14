import java.util.Stack

fun main() {
    fun checkMirror(r: MutableList<MutableList<Char>>): Int {
        for (i in r.indices) {
            if ((r.size - i) % 2 == 1) continue
            val middle = (r.size - i) / 2
            val stacks = List(r.first().size) { Stack<Char>() }
            var flag = true
            for (j in i until r.size) {
                if (j < middle) {
                    r[j].forEachIndexed { index, c -> stacks[index].push(c) }
                } else {
                    r[j].forEachIndexed { index, c -> if (stacks[index].isNotEmpty() && c == stacks[index].peek()) stacks[index].pop() else flag = false }
                }
                if (stacks.all { it.isEmpty() } && flag) return middle + i
            }
            if (stacks.all { it.isEmpty() } && flag) return middle + i
        }
        return 0
    }

    fun getRowOrColumnLine(part: List<String>): Int {
        val rows = mutableListOf<MutableList<Char>>()
        val columns = List<MutableList<Char>>(part.first().length) { mutableListOf() }
        part.forEach { s ->
            rows.add(s.toMutableList())
            s.forEachIndexed { i, c -> columns[i].add(c) }
        }
        val rowMirror = checkMirror(rows)
        val columnsMirror = checkMirror(columns.toMutableList())
        return rowMirror * 100 + columnsMirror
    }

    fun part1(input: List<String>): Int {
        val parts = mutableListOf<List<String>>()
        var temp = mutableListOf<String>()
        input.forEach {
            if (it.isNotBlank()) {
                temp.add(it)
            } else {
                parts.add(temp.toList())
                temp = mutableListOf()
            }
        }
        parts.add(temp)
        return parts.sumOf { getRowOrColumnLine(it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    // check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).println()
    // part2(input).println()
}
