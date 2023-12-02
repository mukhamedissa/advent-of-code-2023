fun getLinesFromFile(name: String): List<String> =
    object {}.javaClass.getResourceAsStream(name)?.bufferedReader()?.readLines() ?: emptyList()

operator fun <K> Map<K, Int>.get(key: K): Int =
    this[key] ?: 0