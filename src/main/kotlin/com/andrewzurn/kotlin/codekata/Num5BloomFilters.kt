package com.andrewzurn.kotlin.codekata

import org.apache.commons.codec.digest.DigestUtils


/**
 * http://codekata.com/kata/kata05-bloom-filters/
 *
 * From link above:
 * Bloom filters are very simple. Take a big array of bits, initially all zero. Then take the things you want to look
 * up (in our case we’ll use a dictionary of words). Produce ‘n’ independent hash values for each word. Each hash is a
 * number which is used to set the corresponding bit in the array of bits. Sometimes there’ll be clashes, where the bit
 * will already be set from some other word. This doesn’t matter.
 *
 * To check to see of a new word is already in the dictionary, perform the same hashes on it that you used to load the
 * bitmap. Then check to see if each of the bits corresponding to these hash values is set. If any bit is not set, then
 * you never loaded that word in, and you can reject it.
 *
 * @author awzurn@gmail.com - 2019-07-11.
 */
class Num5BloomFilters {

    private val dictionary: Map<String, String> = this.javaClass.classLoader.getResource("num-5-bloom-filters-dictionary.txt")
        .readText()
        .split("\n")
        .map { it.toLowerCase() to it }
        .toMap() // as a map for efficient value lookup used below
    private val bloomFilter: List<Int>
    private val numHashes = 8

    init {
        val intermediateBloomFilter = arrayOfNulls<Int>(100_000_000).map { 0 }.toMutableList()
        dictionary.values
            .flatMap { hash(it).asIterable() }
            .forEach { hash -> intermediateBloomFilter[hash] = 1 }
        bloomFilter = intermediateBloomFilter
    }

    fun spellCheck(word: String): Boolean = hash(word).all { bloomFilter[it] == 1 }

    fun find(word: String): String? = dictionary[word.toLowerCase()]

    private fun hash(word: String): List<Int> {
        val hashedWord = DigestUtils.sha1Hex(word.toLowerCase()) // ensures 'Airplane' and 'airplane' will produce same hashes
        return (1..numHashes)
            .map { it * 2 } // adjust the index to ensure we use non-overlapping substrings of the hashed word
            .map { index -> Integer.parseInt(hashedWord.substring(index, index + 6), 16) } // hash then parse a hex string (base 16) to an int
    }
}
