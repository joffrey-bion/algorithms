package org.hildan.algorithms.strings

data class Trie(
    private val next: MutableMap<Char, Trie> = mutableMapOf(),
    private var isLeaf: Boolean = false
) {
    fun add(s: String) {
        if (s.isEmpty()) {
            isLeaf = true
            return
        }
        next.computeIfAbsent(s[0]) { Trie() }.add(s.drop(1))
    }

    fun addAll(strings: Iterable<String>) {
        strings.forEach(::add)
    }

    operator fun get(c: Char): Trie? = next[c]

    operator fun contains(s: String): Boolean =
        if (s.isEmpty()) isLeaf else next[s[0]]?.contains(s.drop(1)) ?: false
}
