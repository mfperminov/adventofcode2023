import kotlin.text.StringBuilder

fun main() {
    fun part1(input: List<String>): Int {
        val columns = List<MutableList<Char>>(input.first().length) { mutableListOf() }
        input.forEach { s ->
            s.forEachIndexed { index, c -> columns[index].add(c) }
        }
        return columns.sumOf {
            var res = 0
            var x = it.size
            var isFirstColumnMet = false
            var nextColumnCoord = it.size
            for (i in it.indices) {
                if (it[i] == '.') continue
                if (it[i] == '#') {
                    isFirstColumnMet = true
                    nextColumnCoord = it.size - i
                }
                if (it[i] == 'O') {
                    if (isFirstColumnMet) {
                        nextColumnCoord--
                        res += nextColumnCoord
                    } else {
                        res += x
                        x--
                    }
                }
            }
            res
        }
    }

    fun part2(input: List<String>): Int {
        var s = input
        val seen = mutableMapOf<List<String>, Int>()
        val sToIndex = mutableMapOf<Int, List<String>>()
        val target = 1_000_000_000
        for (i in 0 until target) {
            val start = seen.put(s, i)
            if (start != null) {
                val size = i - start
                s = sToIndex[start + (target - start) % size]!!
                break
            }
            sToIndex[i] = s
            s = s.north()
            s = s.west()
            s = s.south()
            s = s.east()
        }
        var sum = 0
        val length = s.size
        s.forEachIndexed { i, it ->
            it.forEach {  c ->
                if (c == 'O') { sum += (length-i)}
            }
        }
        return sum
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

fun List<String>.east(): List<String> {
    return map {
        var countBefore = 0
        val sb = StringBuilder(it)
        it.forEachIndexed { index, c ->
            if (c == '#') {
                while (countBefore > 0) {
                    sb[index - countBefore] = 'O'
                    countBefore--
                }
            } else if (c == 'O') {
                sb[index] = '.'
                countBefore++
            }
        }
        while (countBefore > 0) {
            sb[sb.length - countBefore] = 'O'
            countBefore--
        }
        sb.toString()
    }
}

fun List<String>.west(): List<String> {
    return map {
        var countBefore = 0
        var prevIndex = -1
        val sb = StringBuilder(it)
        it.forEachIndexed { index, c ->
            if (c == '#') {
                while (countBefore > 0) {
                    sb[prevIndex + countBefore] = 'O'
                    countBefore--
                }
                prevIndex = index
            } else if (c == 'O') {
                sb[index] = '.'
                countBefore++
            }
        }
        while (countBefore > 0) {
            sb[prevIndex + countBefore] = 'O'
            countBefore--
        }
        sb.toString()
    }
}

fun List<String>.south(): List<String> {
    val sbs = map { StringBuilder(it) }
    val countBefore = Array(first().length) { 0 }
    val prevIndex = Array(first().length) { -1 }
    sbs.mapIndexed { ind, it ->
        for (col in it.indices) {
            if (it[col] == '#') {
                while (countBefore[col] > 0) {
                    sbs[ind - countBefore[col]][col] = 'O'
                    countBefore[col]--
                }
                prevIndex[col] = ind
            } else if (it[col] == 'O') {
                it[col] = '.'
                countBefore[col]++
            }
        }
    }
    countBefore.forEachIndexed { col, countBeforeLeft ->
            var cb = countBeforeLeft
            while (cb > 0) {
                sbs[this.size-cb][col] = 'O'
                cb--
            }
    }
    return sbs.map { it.toString() }
}

fun List<String>.north(): List<String> {
    val sbs = map { StringBuilder(it) }
    val countBefore = Array(first().length) { 0 }
    val prevIndex = Array(first().length) { -1 }
    sbs.mapIndexed { ind, it ->
        for (col in it.indices) {
            if (it[col] == '#') {
                while (countBefore[col] > 0) {
                    sbs[prevIndex[col] + countBefore[col]][col] = 'O'
                    countBefore[col]--
                }
                prevIndex[col] = ind
            } else if (it[col] == 'O') {
                it[col] = '.'
                countBefore[col]++
            }
        }
    }
    countBefore.forEachIndexed { col, countBeforeLeft ->
        var cb = countBeforeLeft
        while (cb > 0) {
            sbs[prevIndex[col] + cb][col] = 'O'
            cb--
        }
    }
    return sbs.map { it.toString() }
}

