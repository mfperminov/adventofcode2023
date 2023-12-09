import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val path = input[0]
        var steps = 0
        var currentKey = "AAA"
        var i = 0
        val map = input.subList(2, input.size).associate(String::toNodeKey)
        while (currentKey != "ZZZ") {
            currentKey = map[currentKey]!!.getNextKey(path[i])
            steps++
            i++
            if (i >= path.length) i = 0
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val path = input[0]
        var steps = 0L
        val map: Map<String, Node> = input.subList(2, input.size).associate(String::toNodeKey)
        var keys = map.keys.filter { it.endsWith("A") }.map { Key(it) }
        keys = keys.map {
            var i = 0
            var k: Key = it
            while (!k.currentKey.endsWith("Z")) {
                k = k.copy(currentKey = map[k.currentKey]!!.getNextKey(path[i % path.length]), divAz = k.divAz + 1)
                i++
            }
            do {
                k = k.copy(currentKey = map[k.currentKey]!!.getNextKey(path[i % path.length]), divZz = k.divZz + 1)
                i++
            } while (!k.currentKey.endsWith("Z"))
            k
        }
        val minAzKey = keys.minBy { it.divAz }
        keys = keys.map { it.copy(diffMax = it.divAz - minAzKey.divAz) }
        val maxZzKey = keys.maxBy { it.divZz }
        steps += maxZzKey.divAz
        var iter = 0L
        while (keys.any { (iter * maxZzKey.divZz + maxZzKey.diffMax - it.diffMax) % it.divZz != 0L }) {
            iter++
        }
        return steps + iter * maxZzKey.divZz
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput) == 2)

    val input = readInput("Day08")
    part1(input).println()
    part2(testInput2).println()
    part2(input).println()
}

private data class Key(val currentKey: String, val divAz: Long = 0L, val divZz: Long = 0L, val diffMax: Long = 0L)

private data class Node(val left: String, val right: String) {
    fun getNextKey(ch: Char): String {
        return if (ch == 'R') right else left
    }
}

private fun String.toNodeKey(): Pair<String, Node> {
    val (key, rawNode) = split(" = ")
    val left = rawNode.substringAfter('(').substringBefore(',')
    val right = rawNode.substringAfter(", ").substringBefore(')')
    return key to Node(left, right)
}
