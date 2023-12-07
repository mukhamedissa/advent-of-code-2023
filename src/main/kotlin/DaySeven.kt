private typealias Cards = String
private typealias ParsedCards = List<Int>
private typealias Bid = Int
private typealias HandBid = Pair<Cards, Bid>

fun main() {
    getLinesFromFile("day_seven.txt").map { line ->
        val (hand, bid) = line.split(" ")
        hand to bid.toInt()
    }.let {
        partOne(it)
        partTwo(it)
    }
}

private val comparator = Comparator<Pair<HandParser, ProcessedHand>> {
        (handOneParser, handOne), (handTwoParser, handTwo) ->
    handOneParser.getType(handOne.parsedCards).compareTo(handTwoParser.getType(handTwo.parsedCards))
        .takeUnless { it == 0 }
        ?: handOne.parsedCards.zip(handTwo.parsedCards)
            .firstOrNull { (firstCard, secondCard) -> firstCard != secondCard }
            ?.let { (firstCard, secondCard) -> firstCard.compareTo(secondCard) } ?: 0
}

private fun partOne(
    handBids: List<HandBid>,
) {
    handBids
        .map { hand ->
            val parser = RegularHandParser(hand)
            parser to parser.parse()
        }
        .sortedWith(comparator)
        .withIndex()
        .sumOf { (idx, hand) -> (idx + 1) * hand.second.bid }
        .also(::println)
}

private fun partTwo(handBids: List<HandBid>) {
    handBids
        .map { hand ->
            val parser = JokerHandParser(hand)
            parser to parser.parse()
        }
        .sortedWith(comparator)
        .withIndex()
        .sumOf { (idx, hand) -> (idx + 1) * hand.second.bid }
        .also(::println)
}

private enum class HandType(val value: Int, val cardCounts: List<Int>) {
    FIVE_OF_A_KIND(6, listOf(5)),
    FOUR_OF_A_KIND(5, listOf(4, 1)),
    FULL_HOUSE(4, listOf(3, 2)),
    THREE_OF_A_KIND(3, listOf(3, 1, 1)),
    TWO_PAIR(2, listOf(2, 2, 1)),
    ONE_PAIR(1, listOf(2, 1, 1, 1)),
    HIGH_CARD(0, listOf(1, 1, 1, 1, 1))
}

data class ProcessedHand(
    val parsedCards: ParsedCards,
    val bid: Bid
)

interface HandParser {

    val cardMap: HashMap<Char, Int>
        get() = hashMapOf(
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'T' to 10,
            'J' to 11,
            'Q' to 12,
            'K' to 13,
            'A' to 14
        )

    fun parse(): ProcessedHand

    fun getType(parsedCards: ParsedCards): Int

    fun countCards(parsedCards: ParsedCards): List<Int> =
        parsedCards.groupingBy { it }
            .eachCount()
            .filterValues { value -> value != 0 }
            .values
            .sortedDescending()
}

class RegularHandParser(private val hand: HandBid) : HandParser {
    override fun parse(): ProcessedHand =
        ProcessedHand(
            parsedCards = hand.first.map(cardMap::getValue),
            bid = hand.second
        )

    override fun getType(parsedCards: ParsedCards): Int =
        HandType
            .entries
            .first { it.cardCounts == countCards(parsedCards) }
            .value
}

class JokerHandParser(private val hand: HandBid) : HandParser {
    override fun parse(): ProcessedHand {
        val jokerCardMap = cardMap.mapValues { (key, value) ->
            if (key == 'J') 0 else value
        }

        return ProcessedHand(
            parsedCards = hand.first.map(jokerCardMap::getValue),
            bid = hand.second
        )
    }

    override fun getType(parsedCards: ParsedCards): Int {
        val jokers = parsedCards.count { it == 0 }
        val cardCountsWithoutJoker = countCards(parsedCards.filterNot { it == 0 })
        val cards = if (cardCountsWithoutJoker.isEmpty())
            listOf(jokers)
        else
            listOf(cardCountsWithoutJoker[0] + jokers) + cardCountsWithoutJoker.drop(1)
        return HandType
            .entries
            .first { it.cardCounts == cards }
            .value
    }
}