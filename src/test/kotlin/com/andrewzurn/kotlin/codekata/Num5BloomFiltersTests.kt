package com.andrewzurn.kotlin.codekata

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class Num5BloomFiltersTests {

    private val TestWords = this.javaClass.classLoader.getResource("num-5-bloom-filters-word-list.txt")
        .readText()
        .split("\n")

    private val classUnderTest = Num5BloomFilters()

    @Test
    fun `can return if a word actually exists in the dictionary or not`() {
        assertTrue(classUnderTest.find("Airplane")?.isNotBlank() ?: false)
        assertTrue(classUnderTest.find("enalpria").isNullOrBlank())
    }

    @Test
    fun `can spell check a word`() {
        assertTrue(classUnderTest.spellCheck("Airplane"))
        assertFalse(classUnderTest.spellCheck("enalpria"))
    }

    @Test
    fun `the bloom filter should be able to spell check a list of words`() {
        TestWords.forEach { testWord ->
            classUnderTest.spellCheck(testWord).let { if (it) println("Word might be correct: $testWord") }
        }
    }

    @Test
    fun `random words should be able to be spell checks and looked up in the original dictionary of words`() {
        val results = (0..15_000)
            .map { RandomStringUtils.randomAlphabetic(5) }
            .withIndex()
            .filter { indexedWord ->
                classUnderTest
                    .spellCheck(indexedWord.value)
                    .also { if (it) println("Word might be correct: ${indexedWord.value} - iter: ${indexedWord.index}") }
            }
            .map { indexedWord ->
                val falsePositive = classUnderTest.find(indexedWord.value).isNullOrBlank()
                if (falsePositive)
                    println("Found a false positive: ${indexedWord.value} - iter: ${indexedWord.index}")
                falsePositive
            }
        assertTrue(results.all { !it }) // should not have found any false positives
    }
}