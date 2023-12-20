fun main() {
    fun part1(input: List<String>): Int {
        var s = ""
        val iter = input.iterator()
        val conditions = LinkedHashMap<String, List<Cond>>()
        do {
            s = iter.next()
            val name = s.substringBefore('{')
            val rawCond = s.substringAfter('{').substringBefore('}')
            conditions[name] = rawCond.split(',').map(Cond::fromString)
        } while (s.isNotEmpty())
        val parts = mutableListOf<Part>()
        do {
            s = iter.next()
            val raw = s.substringAfter('{').substringBefore('}').split(',')
            val map = mutableMapOf<Option, Int>()
            raw.forEach { val (optRaw, value) = it.split('=')
                map[Option.valueOf(optRaw)] = value.toInt()
            }
            parts.add(Part(map))
        } while (iter.hasNext())
        val aparts = mutableListOf<Part>()
        parts.forEach { part ->
            var currentDest: Dest = Dest.AnotherRule("in")
            while (currentDest != Dest.A && currentDest != Dest.R) {
                conditions[(currentDest as Dest.AnotherRule).name]!!.forEach {
                    if (it is Cond.Last) {
                        currentDest = it.dest
                    } else {
                        it as Cond.Normal
                        if (it.sign == '>') {
                            if (part.map[it.option]!! > it.value) {
                                currentDest = it.destination
                            }
                        } else {
                            if (part.map[it.option]!! < it.value) {
                                currentDest = it.destination
                            }
                        }
                    }
                }
                if (currentDest == Dest.A) {
                    aparts.add(part)
                }
            }

        }
        return 0

    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 1)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

private data class Rule(val name: String, val cond: List<Cond>)

private sealed interface Dest {
    data object A: Dest
    data object R: Dest
    data class AnotherRule(val name: String): Dest

    companion object {
        fun fromString(s: String): Dest {
            return when (s) {
                "A" -> A
                "R" -> R
                else -> AnotherRule(s)
            }
        }
    }
}

enum class Option {
    x,
    m,
    a,
    s;
}

private sealed class Cond(val dest: Dest) {
    data class Normal(val sign: Char, val option: Option, val value: Int, val destination: Dest): Cond(destination)
    data class Last(val destination: Dest): Cond(destination)
    companion object {
        fun fromString(s: String): Cond {
            if (!s.contains('>') && !s.contains('<')) {
                return Last(Dest.fromString(s))
            }
            val splitted = s.split('>', ':', '<')
            val opt = Option.valueOf(splitted.first())
            val sign = if (s.contains('>')) '>' else '<'
            val value = splitted[1].toInt()
            val destination = Dest.fromString(splitted.last())
            return Normal(sign, opt, value, destination)
        }
    }
}


private data class Part(val map: Map<Option, Int>)