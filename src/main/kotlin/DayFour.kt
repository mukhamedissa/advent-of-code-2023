import kotlin.math.pow

fun main() {
    getLinesFromFile("day_four.txt").also {
        partOne(it)
        partTwo(it)
    }
}

typealias RawNumbers = String
typealias CardContent = String

private fun partOne(cards: List<String>) {
    cards.sumOf { card ->
        val (_, winningNumbersRaw, myNumbersRaw) = card.extractNumbers()
        (winningNumbersRaw.splitAndProcess() to myNumbersRaw.splitAndProcess()).let { (winning, my) ->
            val winningCount = my.filter(winning::contains).size
            2.0.pow(winningCount - 1).toInt()
        }
    }.also(::println)
}

private fun partTwo(cards: List<String>) {
    val cardInstances = HashMap<Int, Int>().apply {
        repeat(cards.size) {
            this[it + 1] = 1
        }
    }
    cards.forEach { card ->
        val (cardId, winningNumbersRaw, myNumbersRaw) = card.extractNumbers()
        val winningCount = myNumbersRaw.splitAndProcess()
            .filter(winningNumbersRaw.splitAndProcess()::contains).size
        for (i in cardId + 1..(cardId + winningCount)) {
            cardInstances[i] = cardInstances.getValue(i) + cardInstances.getOrDefault(cardId, 0)
        }
    }
    println(cardInstances.values.sum())
}

private fun RawNumbers.splitAndProcess(): HashSet<Int> =
    split(" ")
        .filter(String::isNotEmpty)
        .map(String::toInt)
        .toHashSet()

private fun CardContent.extractNumbers(): Triple<Int, String, String> {
    val (cardNumber, numbers) = split(": ")
    val (winningNumbersRaw, myNumbersRaw) = numbers.split(" | ")
    val cardId = cardNumber.split(" ").filter(String::isNotEmpty)[1].toInt()
    return Triple(cardId, winningNumbersRaw, myNumbersRaw)
}