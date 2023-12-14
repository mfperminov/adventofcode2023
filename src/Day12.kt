fun main() {

    fun part1(input: List<String>): Int {

        val a = input.map { s ->
            s.substringBefore(" ") to s.substringAfter(' ').extractIntNumbers(sep = ',')
        }
        val sum = a.sumOf { (s, condition) ->
            acceptedSplit(s, condition.toMutableList())
        }
        return sum
    }
    // сичтать количество точек

    fun part2(input: List<String>): Long {
        val a = input.map { s ->
            s.substringBefore(" ") to s.substringAfter(' ').extractIntNumbers(sep = ',')
        }
        val sum = a.sumOf { (s, condition) ->
            var orig = s
            repeat(4) {
                orig += "?$s"
            }
            val newCondition = condition.toMutableList()
            repeat(4) {
                newCondition.addAll(condition)
            }
            acceptedSplitWithCache(orig, newCondition, mutableMapOf())
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

private fun acceptedSplit(s: String, groups: MutableList<Int>): Int {
    if (groups.isEmpty()) {
        return if (s.contains('#')) {
            0
        } else {
            1
        }
    }
    if (s.isEmpty()) return 0
    if (s.first() == '.') return acceptedSplit(s.substring(1), groups)
    if (s.first() == '?') {
        return acceptedSplit("." + s.substring(1), groups) +
            acceptedSplit("#" + s.substring(1), groups)
    }
    if (s.first() == '#') {
        if (s.take(groups.first()).any { it == '.' }) return 0
        if (s.length == groups.first()) {
            return if (groups.size == 1) {
                1
            } else {
                0
            }
        }
        if (s.length < groups.first()) return 0
        if (s[groups.first()]== '?' || s[groups.first()]=='.') {
            return acceptedSplit(
                s.substring((groups.first() + 1)),
                groups.subList(1, groups.size)
            )
        }
    }
    return 0
}

private fun acceptedSplitWithCache(s: String, groups: MutableList<Int>, cache: MutableMap<Snapshot, Long>): Long {
    if (cache.contains((Snapshot(s, groups)))) {
        return cache[Snapshot(s, groups)]!!
    }
    if (groups.isEmpty()) {
        return if (s.contains('#')) {
            cache[Snapshot(s, groups)] = 0L
            0
        } else {
            cache[Snapshot(s, groups)] = 1L
            1
        }
    }
    if (s.isEmpty()) return 0
    if (s.first() == '.') return acceptedSplitWithCache(s.substring(1), groups, cache).also { cache[Snapshot(s.substring(1), groups)] = it }
    if (s.first() == '?') {
        return acceptedSplitWithCache("." + s.substring(1), groups, cache).also { cache[Snapshot("." + s.substring(1), groups)] = it } +
            acceptedSplitWithCache("#" + s.substring(1), groups, cache).also { cache[Snapshot("#" + s.substring(1), groups)] = it }
    }
    if (s.first() == '#') {
        if (s.take(groups.first()).any { it == '.' }) return 0
        if (s.length == groups.first()) {
            return if (groups.size == 1) {
                1L
            } else {
                0L
            }
        }
        if (s.length < groups.first()) return 0
        if (s[groups.first()]== '?' || s[groups.first()]=='.') {
            return acceptedSplitWithCache(
                s.substring((groups.first() + 1)),
                groups.subList(1, groups.size),
                cache
            ).also { cache[Snapshot(s.substring((groups.first() + 1)), groups.subList(1, groups.size))] = it }
        }
    }
    return 0
}

private data class Snapshot(val s: String, val groups: List<Int>)
