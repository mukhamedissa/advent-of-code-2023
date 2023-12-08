private typealias Network = List<String>

fun main() {
    val lines = getLinesFromFile("day_eight.txt")
        .filterNot { it.isEmpty() }

    val (instructions, nodeMap) = lines[0] to lines.drop(1).parse()
    partOne(
        instructions = instructions,
        nodeMap = nodeMap
    )
    partTwo(
        instructions = instructions,
        nodeMap = nodeMap
    )
}

private fun partOne(
    instructions: String,
    nodeMap: Map<String, List<String>>
) {
    calculateTotalSteps(
        instructions = instructions,
        nodeMap = nodeMap,
        terminationCondition = { it == "ZZZ" }
    ).let(::println)
}

private fun partTwo(
    instructions: String,
    nodeMap: Map<String, List<String>>
) {
    val startingNodes = nodeMap.filterKeys { node -> node.last() == 'A' }
    startingNodes.map { (node, _) ->
        calculateTotalSteps(
            instructions = instructions,
            nodeMap = nodeMap,
            currentNode = node,
            terminationCondition = { it.last() == 'Z' }
        ).toBigInteger()
    }.reduce { acc, next ->
        acc.multiply(next)
            .abs()
            .divide(acc.gcd(next))
    }.also(::println)
}

private tailrec fun calculateTotalSteps(
    instructions: String,
    nodeMap: Map<String, List<String>>,
    currentNode: String = "AAA",
    currentInstructionIndex: Int = 0,
    steps: Int = 0,
    terminationCondition: (String) -> Boolean
): Int {
    if (terminationCondition.invoke(currentNode)) {
        return steps
    }
    val neighborIndex = if (instructions[currentInstructionIndex] == 'L') 0 else 1
    return calculateTotalSteps(
        instructions = instructions,
        nodeMap = nodeMap,
        currentNode = nodeMap.getValue(currentNode)[neighborIndex],
        currentInstructionIndex = (currentInstructionIndex + 1) % instructions.length,
        steps = steps + 1,
        terminationCondition = terminationCondition
    )
}

private fun Network.parse(): Map<String, List<String>> =
    map {
        val (node, neighbors) = it.split(" = ")
        node to neighbors
    }.associateBy { it.first }
        .mapValues { it.value.second }
        .mapValues { (_, neighbors) ->
            neighbors.substring(1..<neighbors.lastIndex)
                .split(", ")
                .toList()
        }