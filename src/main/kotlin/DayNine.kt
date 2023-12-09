fun main() {
    getLinesFromFile("day_nine.txt")
        .map { line -> line.split(" ") }
        .map { history -> history.map(String::toInt) }
        .let { historyValues ->
            partOne(historyValues)
            partTwo(historyValues)
        }
}

enum class ExtrapolationDirection(
    val addPosition: Int
) {
    FORWARDS(-1), BACKWARDS(0)
}

private fun partOne(historyValues: List<List<Int>>) {
    historyValues
        .extrapolatedSum(
            direction = ExtrapolationDirection.FORWARDS
        ).also(::println)
}

private fun partTwo(historyValues: List<List<Int>>) {
    historyValues
        .extrapolatedSum(
            direction = ExtrapolationDirection.BACKWARDS
        ).also(::println)
}
private fun generateSequences(
    currentSequence: List<Int>,
    sequences: ArrayList<ArrayList<Int>>
): ArrayList<ArrayList<Int>> {
    val nextSequence = currentSequence
        .zipWithNext { first, second -> second - first }
        .also { sequences.add(ArrayList(it)) }

    if (nextSequence.all { it == 0 }) {
        return sequences
    }

    return generateSequences(
        currentSequence = nextSequence,
        sequences = sequences
    )
}

private fun List<List<Int>>.extrapolatedSum(direction: ExtrapolationDirection): Int =
    sumOf { sequence ->
        val generatedSequences =
            generateSequences(
                currentSequence = sequence,
                sequences = arrayListOf(ArrayList(sequence))
            )
        for (i in generatedSequences.indices.reversed()) {
            if (i == generatedSequences.lastIndex) {
                generatedSequences[i].addToPosition(direction.addPosition, 0)
                continue
            }
            val nextSequenceElement =
                if (direction == ExtrapolationDirection.FORWARDS)
                    generatedSequences[i].last() + generatedSequences[i + 1].last()
                else
                    generatedSequences[i][0] - generatedSequences[i + 1][0]

            generatedSequences[i].addToPosition(direction.addPosition, nextSequenceElement)
        }
        when (direction) {
            ExtrapolationDirection.FORWARDS -> generatedSequences[0].last()
            ExtrapolationDirection.BACKWARDS -> generatedSequences[0][0]
        }
    }