import kotlin.math.min
import kotlin.system.measureTimeMillis

private typealias Almanac = List<String>
private typealias Map = List<String>

fun main() {
    val almanac = getLinesFromFile("day_five.txt")
    val seeds = almanac[0].split(" ").drop(1).map(String::toLong)
    val ranges = listOf(
        almanac.extractBlock("seed-to-soil map:").extractRanges(),
        almanac.extractBlock("soil-to-fertilizer map:").extractRanges(),
        almanac.extractBlock("fertilizer-to-water map:").extractRanges(),
        almanac.extractBlock("water-to-light map:").extractRanges(),
        almanac.extractBlock("light-to-temperature map:").extractRanges(),
        almanac.extractBlock("temperature-to-humidity map:").extractRanges(),
        almanac.extractBlock("humidity-to-location map:").extractRanges()
    )
    partOne(seeds, ranges)
    partTwo(seeds, ranges)
}

private fun partOne(
    seeds: List<Long>,
    ranges: List<List<Pair<LongRange, LongRange>>>
) {
    var result = Long.MAX_VALUE
    seeds.forEach { seed ->
        result = min(result, performMapping(seed, ranges))
    }
    println(result)
}

private fun partTwo(
    seeds: List<Long>,
    ranges: List<List<Pair<LongRange, LongRange>>>
) {
    val seedRanges = seeds.chunked(2)
        .map { (start, range) -> start..<(start + range) }

    var result = Long.MAX_VALUE
    val executionTimeInMillis = measureTimeMillis {
        seedRanges.forEach { seedRange ->
            seedRange.forEach { seed ->
                result = min(result, performMapping(seed, ranges))
            }
        }
    }

    println(result)
    println("Execution time: $executionTimeInMillis")
}

private fun performMapping(
    seed: Long,
    ranges: List<List<Pair<LongRange, LongRange>>>
): Long {
    var seed = seed
    ranges.forEach { range ->
        seed = range.lookupSeed(seed)
    }
    return seed
}

private fun Almanac.extractBlock(name: String): List<String> {
    val startIdx = indexOfFirst { it == name }
    var endIdx = startIdx
    while (endIdx < size && this[endIdx].isNotEmpty()) {
        endIdx++
    }
    return subList(startIdx + 1, endIdx)
}

private fun Map.extractRanges(): List<Pair<LongRange, LongRange>> =
    map {
        val (destination, source, range) = it.split(" ").map(String::toLong)
        destination..<(destination + range) to source..<(source + range)
    }

private fun List<Pair<LongRange, LongRange>>.lookupSeed(seed: Long): Long {
    forEach { (destination, source) ->
        if (seed in source) {
            return destination.first - source.first + seed
        }
    }
    return seed
}