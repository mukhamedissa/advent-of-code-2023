import java.math.BigInteger

private typealias Race = Pair<BigInteger, BigInteger>
private typealias Line = String

fun main() {
    getLinesFromFile("day_six.txt").let { lines ->
        val times = lines[0].extractNumbers()
        val distances = lines[1].extractNumbers()
        val races = times.zip(distances)
        partOne(races)

        val time = times.joinToString("").toBigInteger()
        val distance = distances.joinToString("").toBigInteger()
        partTwo(time to distance)
    }
}

private fun partOne(races: List<Race>) {
    races.map(::simulateRace)
        .reduce { acc, waysToWin -> acc  * waysToWin }
        .also(::println)
}

private fun partTwo(race: Race) {
    simulateRace(race).also(::println)
}

private fun simulateRace(race: Race): Int =
    IntArray(race.first.plus(1.toBigInteger()).toInt()) { it }
        .filter { buttonHeldFor ->
            (race.first.minus(buttonHeldFor.toBigInteger())) * buttonHeldFor.toBigInteger() > race.second
        }.size

private fun Line.extractNumbers(): List<BigInteger> =
    split(" ")
        .mapNotNull(String::toIntOrNull)
        .map { number -> number.toBigInteger() }