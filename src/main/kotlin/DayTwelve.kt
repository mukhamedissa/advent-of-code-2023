fun main() {
    getLinesFromFile("day_twelve.txt").let {
        partOne(it)
        partTwo(it)
    }
}

private enum class SpringState(val value: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?')
}

private fun partOne(records: List<String>) {
    records.sumOf { record ->
        val (springs, arrangement) = record.split(" ")
        val arrangements = arrangement.split(",").map(String::toInt)
        countArrangements(
            cache = hashMapOf(),
            springs = springs.toCharArray(),
            arrangements = arrangements.toIntArray(),
            springPosition = 0,
            arrangementPosition = 0,
            current = 0
        )
    }.also(::println)
}

private fun partTwo(records: List<String>) {
    records.sumOf { record ->
        val (springs, arrangement) = record.split(" ")
        val arrangements = arrangement.split(",").map(String::toInt)
        val repeatedSprings = buildList {
            repeat(5) {
                add(springs)
            }
        }.joinToString("?").toCharArray()
        val repeatedAmounts = buildList {
            repeat(5) {
                add(arrangements)
            }
        }.flatten().toIntArray()
        countArrangements(
            cache = hashMapOf(),
            springs = repeatedSprings,
            arrangements = repeatedAmounts,
            springPosition = 0,
            arrangementPosition = 0,
            current = 0
        )
    }.also(::println)
}

private fun countArrangements(
    cache: HashMap<Triple<Int, Int, Int>, Long>,
    springs: CharArray,
    arrangements: IntArray,
    springPosition: Int,
    arrangementPosition: Int,
    current: Int
): Long {
    val key = Triple(springPosition, arrangementPosition, current)
    if (cache.containsKey(key)) {
        return cache.getValue(key)
    }
    if (springPosition == springs.size) {
        return if (arrangementPosition == arrangements.size && current == 0
            || arrangementPosition == arrangements.lastIndex && arrangements[arrangementPosition] == current)
            1 else 0
    }
    var result = 0L
    if (springs[springPosition] != SpringState.DAMAGED.value && current == 0) {
        result += countArrangements(
            cache = cache,
            springs = springs,
            arrangements = arrangements,
            springPosition = springPosition + 1,
            arrangementPosition = arrangementPosition,
            current = 0
        )
    } else if (springs[springPosition] != SpringState.DAMAGED.value && current > 0 &&
        arrangementPosition < arrangements.size && arrangements[arrangementPosition] == current) {
        result += countArrangements(
            cache = cache,
            springs = springs,
            arrangements = arrangements,
            springPosition = springPosition + 1,
            arrangementPosition = arrangementPosition + 1,
            current = 0
        )
    }
    if (springs[springPosition] != SpringState.OPERATIONAL.value) {
        result += countArrangements(
            cache = cache,
            springs = springs,
            arrangements = arrangements,
            springPosition = springPosition + 1,
            arrangementPosition = arrangementPosition,
            current = current + 1
        )
    }

    return result.also { cache[key] = it }
}