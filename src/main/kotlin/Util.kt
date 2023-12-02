fun getLinesFromFile(name: String): List<String> =
    object {}.javaClass.getResourceAsStream(name)?.bufferedReader()?.readLines() ?: emptyList()

fun String.toIntOr(block: () -> Int): Int {
    return toIntOrNull() ?: block()
}

operator fun <K> Map<K, Int>.get(key: K): Int =
    this[key] ?: 0