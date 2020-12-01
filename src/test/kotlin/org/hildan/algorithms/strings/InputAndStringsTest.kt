package org.hildan.algorithms.strings

import kotlin.test.*

class InputAndStringsTest {

    @Test
    fun digits() {
        assertEquals(emptyList(), "".digits())
        assertEquals(listOf(9), "9".digits())
        assertEquals(listOf(4, 2), "42".digits())
        assertEquals(listOf(1, 2, 3), "123".digits())
        assertEquals(listOf(2, 1, 6, 7, 5), "21675".digits())
        assertFailsWith<NumberFormatException> { "-12".digits() }
    }

    @Test
    fun asciiChars() {
        assertEquals(emptyList(), "".asciiChars())
        assertEquals(listOf('a'.toInt(), 's'.toInt(), 'c'.toInt(), 'i'.toInt(), 'i'.toInt()), "ascii".asciiChars())
    }

    @Test
    fun permutations() {
        assertEquals(setOf(""), permutations(""))
        assertEquals(setOf("a"), permutations("a"))
        assertEquals(setOf("ab", "ba"), permutations("ab"))
        assertEquals(setOf("abc", "acb", "bac", "bca", "cab", "cba"), permutations("abc"))
    }
}