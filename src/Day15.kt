fun main() {
    fun part1(input: List<String>): Long {
        return input.first().split(',').sumOf { s ->
            var sum = 0L
            s.forEach { c ->
                sum += c.code
                sum *= 17
                sum = sum and 0xFF
            }
            sum
        }
    }

    fun part2(input: List<String>): Int {
        val boxes = Array<MutableList<Lens>>(256) { mutableListOf() }
        input.first().split(',').forEach { s ->
            val l = Lens.fromString(s)
            if (l.value == null) {
                boxes[l.HASH].removeAll { it.label == l.label }
            } else {
                if (boxes[l.HASH].firstOrNull { it.label == l.label } != null) {
                    boxes[l.HASH].replaceAll { if (it.label == l.label) it.copy(value = l.value) else it }
                } else {
                    boxes[l.HASH].add(l)
                }
            }
        }
        return boxes.mapIndexed { boxNum, lens ->
            var sum = 0
            lens.forEachIndexed { slot, l ->
                sum += (boxNum+1) * (slot + 1) * l.value!!
            }
            sum
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)

    val input = readInput("Day15")
    part1(input).println()
    check(part2(testInput)==145)
    part2(input).println()
}

data class Lens(val label: String, val value: Int?) {

    val HASH: Int
        get() {
            var sum = 0
            label.forEach {
                sum += it.code
                sum *= 17
                sum = sum and 0xFF
            }
            return sum
        }

    companion object {
        fun fromString(s: String): Lens {
            return if (s.contains('=')) {
                Lens(s.substringBefore('='), s.substringAfter('=').toInt())
            } else {
                Lens(s.substringBefore('-'), null)
            }
        }
    }
}
