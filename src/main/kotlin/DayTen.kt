import java.util.LinkedList

fun main() {
    getLinesFromFile("day_ten.txt")
        .map { it.toCharArray() }
        .let { grid ->
            partTwo(grid, partOne(grid))
        }
}

private fun partOne(grid: List<CharArray>): HashSet<Pair<Int, Int>> {
    val (north, south, east, west) = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
    val directions = mapOf(
        '|' to listOf(north, south),
        '-' to listOf(east, west),
        'L' to listOf(north, east),
        'J' to listOf(north, west),
        '7' to listOf(south, west),
        'F' to listOf(south, east),
        '.' to listOf()
    )

    var (startX, startY)  = (-1 to -1)
    for (i in grid.indices) {
        if (startX != -1 && startY != -1) {
            break
        }
        for (j in grid[i].indices) {
            if (grid[i][j] == 'S') {
                startX = i
                startY = j
                break
            }
        }
    }

    val queue = LinkedList<Pair<Pair<Int, Int>, Int>>()
    val visited = hashSetOf(startX to startY)
    if (north in directions.getValue(grid[startX + 1][startY])) {
        queue.offer((startX + 1 to startY) to 1)
        visited.add(startX + 1 to startY)
    }
    if (south in directions.getValue(grid[startX - 1][startY])) {
        queue.offer((startX - 1 to startY) to 1)
        visited.add(startX - 1 to startY)
    }
    if (east in directions.getValue(grid[startX][startY - 1])) {
        queue.offer((startX to startY - 1) to 1)
        visited.add(startX to startY - 1)
    }
    if (west in directions.getValue(grid[startX][startY + 1])) {
        queue.offer((startX to startY + 1) to 1)
        visited.add(startX to startY + 1)
    }

    var steps = 0
    while (queue.isNotEmpty()) {
        val element = queue.pop()
        val (x, y) = element.first
        steps = element.second
        directions[grid[x][y]]?.forEach { (offsetX, offsetY) ->
            val next = x + offsetX to y + offsetY
            if (next !in visited) {
                queue.offer(next to steps + 1)
                visited.add(next)
            }
        }
    }
    println(steps)
    return visited
}

private fun partTwo(
    grid: List<CharArray>,
    visited: HashSet<Pair<Int, Int>>
) {
    val down = hashSetOf('|', '7', 'F', 'S')
    var enclosedCount = 0
    for (i in grid.indices) {
        var up = false
        for (j in grid[i].indices) {
            if (grid[i][j] in down && visited.contains(i to j)) {
                up = !up
            }
            if (up && !visited.contains(i to j)) {
                enclosedCount++
            }
        }
    }
    println(enclosedCount)
}