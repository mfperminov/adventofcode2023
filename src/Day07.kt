fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val (rawCards, rawId) = it.split(" ")
            val cards = rawCards.map(Card::fromChar)
            Hand(cards, rawId.toInt())
        }.sorted().zip(1..input.size).fold(0) { acc, pair ->
            pair.first.id * pair.second + acc
        }

    }

    fun part2(input: List<String>): Int {

        // // 251037509 251008389
        return input.map {
            val (rawCards, rawId) = it.split(" ")
            val cards = rawCards.map(JokerCards::fromChar)
            JokerHand(cards, rawId.toInt())
        }.sorted().zip(1..input.size).fold(0) { acc, pair ->
            pair.first.id * pair.second + acc
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    check(part2(testInput) == 5905)
    part2(input).println()
}

private enum class Card(val ch: Char) {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    T('T'),
    J('J'),
    Q('Q'),
    K('K'),
    A('A');

    companion object {
        fun fromChar(value: Char) = Card.values().first { it.ch == value }
    }
}

private enum class JokerCards(val ch: Char) {
    J('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    T('T'),
    Q('Q'),
    K('K'),
    A('A');

    override fun toString(): String {
        return ch.toString()
    }

    companion object {
        fun fromChar(value: Char) = JokerCards.values().first { it.ch == value }
    }
}

private enum class HandsType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfKind,
    FullHouse,
    FourOfKind,
    FiveOfKind;
}

private data class Hand(val cards: List<Card>, val id: Int) : Comparable<Hand> {
    val type: HandsType
        get() {
            val grouped: Map<Char, List<Card>> = cards.groupBy { it.ch }
            if (grouped.size == 1) return HandsType.FiveOfKind
            if (grouped.size == 2 && grouped.any { it.value.size == 4 }) return HandsType.FourOfKind
            if (grouped.size == 2 && grouped.any { it.value.size == 3 } && grouped.any { it.value.size == 2 }) return HandsType.FullHouse
            if (grouped.any { it.value.size == 3 }) return HandsType.ThreeOfKind
            if (grouped.size == 3 && grouped.count { it.value.size == 2 } == 2) return HandsType.TwoPair
            if (grouped.size == 4) return HandsType.OnePair
            return HandsType.HighCard
        }

    override fun compareTo(other: Hand): Int {
        if (type.compareTo(other.type) != 0) {
            return type.compareTo(other.type)
        } else {
            for (i in cards.indices) {
                if (cards[i] != other.cards[i]) {
                    return cards[i].compareTo(other.cards[i])
                }
            }
        }
        return 0
    }
}

private data class JokerHand(val cards: List<JokerCards>, val id: Int) : Comparable<JokerHand> {
    val type: HandsType
        get() {
            val grouped: Map<Char, List<JokerCards>> = cards.groupBy { it.ch }
            if (grouped.size == 1) return HandsType.FiveOfKind
            if (grouped.size == 2 && grouped.any { it.value.size == 4 }) return HandsType.FourOfKind
            if (grouped.size == 2 && grouped.any { it.value.size == 3 } && grouped.any { it.value.size == 2 }) return HandsType.FullHouse
            if (grouped.any { it.value.size == 3 }) return HandsType.ThreeOfKind
            if (grouped.size == 3 && grouped.count { it.value.size == 2 } == 2) return HandsType.TwoPair
            if (grouped.size == 4) return HandsType.OnePair
            return HandsType.HighCard
        }

    fun jokerType(): HandsType {
        val grouped: Map<Char, List<JokerCards>> = cards.groupBy { it.ch }
        return when (type) {
            HandsType.HighCard -> {
                if (cards.contains(JokerCards.J)) {
                    HandsType.OnePair
                } else {
                    HandsType.HighCard
                }
            }
            HandsType.OnePair -> {
                if (grouped.values.any { it.size == 2 && it.contains(JokerCards.J) }) {
                    HandsType.ThreeOfKind
                } else if (grouped.values.any { it.size == 1 && it.contains(JokerCards.J) }) {
                    HandsType.ThreeOfKind
                } else {
                    HandsType.OnePair
                }
            }
            HandsType.TwoPair -> {
                if (grouped.values.any { it.size == 2 && it.contains(JokerCards.J) }) {
                    HandsType.FourOfKind
                } else if (grouped.values.any { it.size == 1 && it.contains(JokerCards.J) }) {
                    HandsType.FullHouse
                } else {
                    HandsType.TwoPair
                }
            }
            HandsType.ThreeOfKind -> {
                if (grouped.values.any { it.contains(JokerCards.J) }) HandsType.FourOfKind else HandsType.ThreeOfKind
            }
            HandsType.FullHouse -> {
                if (grouped.values.any { it.contains(JokerCards.J) }) HandsType.FiveOfKind else HandsType.FullHouse
            }

            HandsType.FourOfKind -> {
                if (grouped.values.any { it.contains(JokerCards.J) }) HandsType.FiveOfKind else HandsType.FourOfKind
            }

            HandsType.FiveOfKind -> HandsType.FiveOfKind
        }
    }

    override fun compareTo(other: JokerHand): Int {
        if (this.jokerType().compareTo(other.jokerType()) != 0) {
            return this.jokerType().compareTo(other.jokerType())
        } else {
            for (i in cards.indices) {
                if (cards[i] != other.cards[i]) {
                    return cards[i].compareTo(other.cards[i])
                }
            }
        }
        return 0
    }
}

