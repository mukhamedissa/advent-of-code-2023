import kotlin.math.abs

private typealias GalaxyPosition = Pair<Long, Long>
private typealias Image = MutableList<MutableList<Char>>

fun main() {
    getLinesFromFile("day_eleven.txt")
        .map { it.toMutableList() }
        .toMutableList()
        .let { image ->
            partOne(image)
            partTwo(image)
        }
}

private fun partOne(image: Image) =
    image.findGalaxyDistanceSumAfterExpansion(expansionFactor = 2).also(::println)

private fun partTwo(image: Image) =
    image.findGalaxyDistanceSumAfterExpansion(expansionFactor = 1_000_000).also(::println)

private fun Image.findGalaxyDistanceSumAfterExpansion(
    expansionFactor: Int
): Long =
    findGalaxyPositions()
        .expand(
            factor = expansionFactor,
            rowsWithoutGalaxies = findRowsWithoutGalaxies(),
            columnsWithoutGalaxies = findColumnsWithoutGalaxies()
        ).generateGalaxyPairs().distanceSum()

private fun Image.findRowsWithoutGalaxies(): List<Int> =
    withIndex()
        .filter { !it.value.contains('#') }
        .map { it.index }

private fun Image.findColumnsWithoutGalaxies(): List<Int> {
    val columns = ArrayList<Int>()

    for (i in this[0].indices) {
        val column = CharArray(size) { this[it][i] }
        if (!column.contains('#')) {
           columns.add(i)
        }
    }

    return columns
}

private fun HashMap<GalaxyPosition, GalaxyPosition>.generateGalaxyPairs(): HashSet<Pair<GalaxyPosition, GalaxyPosition>> {
    val galaxyPairs = HashSet<Pair<GalaxyPosition, GalaxyPosition>>()
    val positions = ArrayList(values)
    for (i in positions.indices) {
        for (j in positions.indices) {
            if (i == j) {
                continue
            }
            if (galaxyPairs.contains(positions[i] to positions[j])
                || galaxyPairs.contains(positions[j] to positions[i])
            ) {
                continue
            }
            galaxyPairs.add(positions[i] to positions[j])
        }
    }
    return galaxyPairs
}

private fun Image.findGalaxyPositions(): HashMap<GalaxyPosition, GalaxyPosition> {
    val galaxyMap = HashMap<GalaxyPosition, GalaxyPosition>()
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j] == '#') {
                val position = i.toLong() to j.toLong()
                galaxyMap[position] = position
            }
        }
    }
    return galaxyMap
}

private fun HashMap<GalaxyPosition, GalaxyPosition>.expand(
    factor: Int,
    rowsWithoutGalaxies: List<Int>,
    columnsWithoutGalaxies: List<Int>
): HashMap<GalaxyPosition, GalaxyPosition> {
    for (i in rowsWithoutGalaxies) {
        for ((current, new) in this) {
            if (current.first > i) {
                this[current] = (new.first + factor - 1) to new.second
            }
        }
    }

    for (i in columnsWithoutGalaxies) {
        for ((current, new) in this) {
            if (current.second > i) {
                this[current] = new.first to (new.second + factor - 1)
            }
        }
    }

    return this
}

private fun HashSet<Pair<GalaxyPosition, GalaxyPosition>>.distanceSum(): Long =
    sumOf { (firstGalaxy, secondGalaxy) ->
        abs(firstGalaxy.first - secondGalaxy.first) + abs(firstGalaxy.second - secondGalaxy.second)
    }