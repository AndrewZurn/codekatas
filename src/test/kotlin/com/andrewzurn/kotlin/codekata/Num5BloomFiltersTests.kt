package com.andrewzurn.kotlin.codekata

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Num5BloomFiltersTests {

    private val TestWords = this.javaClass.classLoader.getResource("num-5-bloom-filters-word-list.txt")
        .readText()
        .split("\n")

    private lateinit var classUnderTest: Num5BloomFilters

    @BeforeAll
    internal fun setUp() {
        classUnderTest = Num5BloomFilters()
    }

    @Test
    fun `can return if a word actually exists in the dictionary or not`() {
        assertTrue(classUnderTest.find("Airplane")?.isNotBlank() ?: false)
        assertTrue(classUnderTest.find("enalpria").isNullOrBlank())
    }

    @ParameterizedTest
    @CsvSource(
        "Airplane, true",
        "enalpria, false"
    )
    fun `can spell check a word`(input: String, expected: Boolean) {
        assert(classUnderTest.spellCheck(input) == expected)
    }

    @Test
    fun `the bloom filter should be able to spell check a list of words`() {
        TestWords.forEach { testWord ->
            classUnderTest.spellCheck(testWord).let { if (it) println("Word might be correct: $testWord") }
        } // note: doesn't actually assert anything
    }

    @Test
    fun `random words should be able to be spell checks and looked up in the original dictionary of words`() {
        val results = (1..25_000)
            .map { RandomStringUtils.randomAlphabetic(5) } // create 'n' random 5-character length word
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
