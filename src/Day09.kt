fun main() {
    fun part1(input: List<String>): Long {
        val seq: List<Long> = input.map { s->
            val r = mutableListOf<Sequence>()
            val sc = s.extractLongNumbers()
            var s = Sequence(sc)
            r.add(s)
            while (!s.isLast()) {
                r.add(s.diffs())
                s = s.diffs()
            }
            var acc = 0L
            r.forEach { acc += it.value.last() }
            acc
        }
        return seq.sum()
    }

    fun part2(input: List<String>): Long {
        val seq: List<Long> = input.map { s->
            val r = mutableListOf<Sequence>()
            val sc = s.extractLongNumbers()
            var s = Sequence(sc)
            r.add(s)
            while (!s.isLast()) {
                r.add(s.diffs())
                s = s.diffs()
            }
            var acc = 0L
            r.reversed().forEach { acc = it.value.first() - acc }
            acc
        }
        return seq.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)

    val input = readInput("Day09")
    part1(input).println()
    check(part2(testInput) == 2L)
    part2(input).println()
}

data class Sequence(val value: List<Long>) {
    fun diffs(): Sequence {
        val newValue = mutableListOf<Long>()
        for (i in value.lastIndex downTo 1) {
            newValue.add(value[i]-value[i-1])
        }
        return Sequence(newValue.reversed())
    }

    fun isLast() = value.all { it == 0L }
    
}
