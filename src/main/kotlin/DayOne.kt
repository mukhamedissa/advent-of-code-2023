fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    getLinesFromFile("day_one.txt").sumOf { line ->
        line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
    }.also(::println)
}

typealias PositionToMatchedString = Pair<Int, String>

private fun partTwo() {
    val digitMapping = hashMapOf(
        "one"   to 1,
        "two"   to 2,
        "three" to 3,
        "four"  to 4,
        "five"  to 5,
        "six"   to 6,
        "seven" to 7,
        "eight" to 8,
        "nine"  to 9
    )

    fun PositionToMatchedString?.convertOrGetMappedValue(): Int =
        this?.second?.toIntOrNull() ?: (digitMapping[this?.second] ?: 0)

    val digits = digitMapping.keys + digitMapping.values.map(Int::toString)
    getLinesFromFile("day_one.txt").sumOf { line ->
        val firstDigit = line.findAnyOf(digits).convertOrGetMappedValue()
        val lastDigit = line.findLastAnyOf(digits).convertOrGetMappedValue()
        firstDigit * 10 + lastDigit
    }.also(::println)
}

