import kotlin.math.abs

private typealias GalaxyPosition = Pair<Long, Long>
private typealias Image = MutableList<MutableList<Char>>

private fun getImageFromFile(): MutableList<MutableList<Char>> =
    getLinesFromFile("day_eleven.txt")
        .map { it.toMutableList() }
        .toMutableList()

fun main() {
    partOne(getImageFromFile())
    partTwo(getImageFromFile())
}

private fun partOne(image: Image) {
    val rowsWithoutGalaxies = image.findRowsWithoutGalaxies().reversed()
    val columnsWithoutGalaxies = image.findColumnsWithoutGalaxies().reversed()

    for (i in rowsWithoutGalaxies) {
        image.add(i, CharArray(image[0].size) { '.' }.toMutableList())
    }

    for (i in columnsWithoutGalaxies) {
        for (j in image.indices) {
            image[j].add(i, '.')
        }
    }

    ArrayList(image.findGalaxyPositions().values)
        .generateGalaxyPairs()
        .distanceSum()
        .also(::println)
}

private fun partTwo(image: Image) {
    val rowsWithoutGalaxies = image.findRowsWithoutGalaxies().reversed()
    val columnsWithoutGalaxies = image.findColumnsWithoutGalaxies().reversed()
    val galaxyMap = image.findGalaxyPositions()

    for (i in rowsWithoutGalaxies) {
        for ((current, new) in galaxyMap) {
            if (current.first > i) {
                galaxyMap[current] = (new.first + 999_999) to new.second
            }
        }
    }

    for (i in columnsWithoutGalaxies) {
        for ((current, new) in galaxyMap) {
            if (current.second > i) {
                galaxyMap[current] = new.first to (new.second + 999_999)
            }
        }
    }

    ArrayList(galaxyMap.values.toList())
        .generateGalaxyPairs()
        .distanceSum()
        .also(::println)
}

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

private fun ArrayList<GalaxyPosition>.generateGalaxyPairs(): HashSet<Pair<GalaxyPosition, GalaxyPosition>> {
    val galaxyPairs = HashSet<Pair<GalaxyPosition, GalaxyPosition>>()
    for (i in indices) {
        for (j in indices) {
            if (i == j) {
                continue
            }
            if (galaxyPairs.contains(this[i] to this[j])
                || galaxyPairs.contains(this[j] to this[i])
            ) {
                continue
            }
            galaxyPairs.add(this[i] to this[j])
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

private fun HashSet<Pair<GalaxyPosition, GalaxyPosition>>.distanceSum(): Long =
    sumOf { (firstGalaxy, secondGalaxy) ->
        abs(firstGalaxy.first - secondGalaxy.first) + abs(firstGalaxy.second - secondGalaxy.second)
    }