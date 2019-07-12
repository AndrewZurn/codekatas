package com.andrewzurn.kotlin.codekata

/**
 * @author awzurn@gmail.com - 2019-07-11.
 */
class Num5BloomFilters {

    private val dictionary = this.javaClass.classLoader.getResource("num-5-bloom-filters-dictionary.txt")
        .readText()
        .split("\n")
        .map { it.toLowerCase() to it }
        .toMap()
    private val bloomFilter: String? = null

    fun spellCheck(word: String): Boolean {
        return find(word)?.isNotBlank() ?: false
    }

    fun find(word: String): String? {
        return dictionary[word.toLowerCase()]
    }
}