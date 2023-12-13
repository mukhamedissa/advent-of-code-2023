fun main() {
    getTextFromFile("day_thirteen.txt")?.let {
        partOne(it)
        partTwo(it)
    }
}

private fun partOne(input: String) =
    summarize(input = input, hasSmudge = false).also(::println)

private fun partTwo(input: String) =
    summarize(input = input, hasSmudge = true).also(::println)

private fun summarize(input: String, hasSmudge: Boolean): Int =
    input.split("\r\n\r\n").sumOf { chunk ->
        val pattern = chunk.split("\r\n")
        val horizontalMirror = findHorizontalReflection(pattern, if (hasSmudge) 1 else 0)
        val verticalMirror = findHorizontalReflection(pattern.rotate(), if (hasSmudge) 1 else 0)
        horizontalMirror * 100 + verticalMirror
    }

private fun findHorizontalReflection(
    pattern: List<String>,
    smudges: Int = 0
): Int {
    for (mirrorLocation in 1..<pattern.size) {
        var (left, right) = mirrorLocation to mirrorLocation - 1
        var diffCount = 0

        while (left < pattern.lastIndex && right > 0 && diffCount <= smudges) {
            diffCount += diff(pattern[left], pattern[right])
            left = left.inc()
            right = right.dec()
        }
        diffCount += diff(pattern[left], pattern[right])
        if (diffCount == smudges) {
            return mirrorLocation
        }
    }
    return 0
}

private fun List<String>.rotate(): List<String> =
    buildList {
        for (i in this@rotate[0].indices) {
            this += CharArray(this@rotate.size) { this@rotate[it][i] }.joinToString("")
        }
    }

private fun diff(a: String, b: String): Int =
    a.zip(b).sumOf { (aChar, bChar) -> (aChar != bChar).toInt() }