fun main() {
    partOne()
    partTwo()
}

private const val RED = "red"
private const val GREEN = "green"
private const val BLUE = "blue"

private fun String.extractCubesWithAmounts(): Map<String, List<Int>> =
    split(";")
        .flatMap { set -> set.split(",") }
        .map { revealedCube ->
            revealedCube.trim().split(" ").let { (amount, color) ->
                color to amount
            }
        }
        .groupBy({ it.first }, { it.second.toInt() })

typealias RevealedCubes = Map<String, List<Int>>
private fun partOne() {
    val possibleGameParams = hashMapOf(RED to 12, GREEN to 13, BLUE to 14)

    fun RevealedCubes.isGamePossible(color: String): Boolean =
        getValue(color).max() <= possibleGameParams.getValue(color)

    getLinesFromFile("day_two.txt").map { line ->
        val (game, revealedSets) = line.split(":")
        val gameId = game.split(" ")[1].toInt()
        gameId to revealedSets.extractCubesWithAmounts()
    }.sumOf { (gameId, cubes) ->
        if (cubes.isGamePossible(RED) && cubes.isGamePossible(GREEN) && cubes.isGamePossible(BLUE))
            gameId
        else
            0
    }.also(::println)
}

private fun partTwo() {
    getLinesFromFile("day_two.txt").sumOf { line ->
        val revealedSets = line.split(":")[1]
        revealedSets.extractCubesWithAmounts()
            .mapValues { (_, amounts) -> amounts.max() }
            .map { it.value }
            .reduce { accumulator, amount -> accumulator * amount }
    }.also(::println)
}