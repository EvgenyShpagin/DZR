package com.music.dzr.core.network.test

/**
 * Converts a list of strings into a JSON array formatted string.
 * For example, `listOf("a", "b")` will be converted to `["a","b"]`.
 */
fun List<String>.toJsonArray(): String {
    val builder = StringBuilder()
    builder.append('[')
    forEach {
        builder.append("\"$it\"")
        if (it != last()) {
            builder.append(",")
        }
    }
    builder.append(']')
    return builder.toString()
}

/**
 * Helper function to get response json body from assets file
 */
fun getJsonBodyAsset(filename: String): String {
    return AssetManager.open(filename).use { inputStream ->
        inputStream.bufferedReader().use { it.readText() }
    }
}