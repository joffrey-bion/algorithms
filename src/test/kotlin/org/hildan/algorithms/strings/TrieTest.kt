package org.hildan.algorithms.strings

import kotlin.test.*

class TrieTest {

    @Test
    fun basicStringTrie() {
        val trie = Trie()
        trie.add("ab")
        trie.add("abcd")
        trie.add("e")

        assertTrue(trie.contains("ab"))
        assertTrue(trie.contains("abcd"))
        assertTrue(trie.contains("e"))

        assertFalse(trie.contains("bob"))
    }

    @Test
    fun trieMap() {
        val trie = TrieMap<Int>()
        trie.put("", 1)
        trie.put("ab", 42)
        trie.put("abcd", 12)
        trie.put("e", 5)

        assertTrue(trie.contains(""))
        assertTrue(trie.contains("ab"))
        assertTrue(trie.contains("abcd"))
        assertTrue(trie.contains("e"))

        assertEquals(1, trie[""])
        assertEquals(42, trie["ab"])
        assertEquals(12, trie["abcd"])
        assertEquals(5, trie["e"])

        assertFalse(trie.contains("bob"))
        assertNull(trie["bob"])
    }

    @Test
    fun trieMap_decodeStream() {
        val trie = TrieMap<Int>()
        trie.put("ab", 42)
        trie.put("bcd", 12)
        trie.put("e", 5)

        assertEquals(listOf(12, 42, 5, 42), trie.decodePrefixStream("bcdabeab"))
    }
}