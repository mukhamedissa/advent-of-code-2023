fun getLinesFromFile(name: String): List<String> =
    object {}.javaClass.getResourceAsStream(name)?.bufferedReader()?.readLines() ?: emptyList()

fun ArrayList<Int>.addToPosition(
    position: Int = -1,
    value: Int
) {
    if (position == -1) {
        add(value)
    } else {
        add(position, value)
    }
}