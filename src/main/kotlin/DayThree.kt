fun main() {
    val engine = getLinesFromFile("day_three.txt").map(String::toList)
    partOne(engine)
    partTwo(engine)
}

private fun Char.isSymbol(): Boolean = this != '.' && !this.isDigit()

private fun List<List<Char>>.areBoundsCorrect(i: Int, j: Int): Boolean =
    i >= 0 && i <= size - 1 && j >= 0 && j <= this[i].size - 1

val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1, -1 to -1, -1 to 1, 1 to -1, 1 to 1)

private fun partOne(engine: List<List<Char>>) {
    var result = 0
    for (i in engine.indices) {
        var j = 0
        while (j < engine[i].size - 1) {
            val numberBuilder = StringBuilder()
            if (!engine[i][j].isDigit()) {
                j++
                continue
            }
            val adjacencyList = ArrayList<Boolean>()
            while (j < engine[i].size && engine[i][j].isDigit()) {
                directions.forEach { (dirI, dirJ) ->
                    if (engine.areBoundsCorrect(i + dirI, j + dirJ)) {
                        adjacencyList.add(engine[i + dirI][j + dirJ].isSymbol())
                    }
                }
                numberBuilder.append(engine[i][j])
                j++
            }
            if (adjacencyList.any { it }) {
                result += numberBuilder.toString().toInt()
            }
            j++
        }
    }
    println(result)
}

private fun partTwo(engine: List<List<Char>>) {
    var result = 0
    for (i in engine.indices) {
        var j = 0
        while (j < engine[i].size) {
            if (engine[i][j] != '*') {
                j++
                continue
            }
            val adjacentNumbers = HashSet<Int>()
            directions.forEach { (dirI, dirJ) ->
                if (engine.areBoundsCorrect(i + dirI, j + dirJ) && engine[i + dirI][j + dirJ].isDigit()) {
                    adjacentNumbers.add(extractNumberForPosition(engine,i + dirI, j + dirJ))
                }
            }
            if (adjacentNumbers.size == 2) {
                result += adjacentNumbers.reduce { acc, number -> acc * number }
            }
            j++
        }
    }
    println(result)
}

private fun extractNumberForPosition(
    engine: List<List<Char>>,
    i: Int,
    j: Int
): Int {
    val numberBuilder = StringBuilder()
    var startJ = j
    while (startJ >= 0 && engine[i][startJ].isDigit()) {
        startJ--
    }
    startJ++
    while (startJ < engine[i].size && engine[i][startJ].isDigit()) {
        numberBuilder.append(engine[i][startJ])
        startJ++
    }
    return numberBuilder.toString().toInt()
}