package org.hildan.algorithms.strings

data class Trie(
    private val next: MutableMap<Char, Trie> = mutableMapOf(),
    private var isLeaf: Boolean = false
) {
    fun add(s: CharSequence) {
        if (s.isEmpty()) {
            isLeaf = true
            return
        }
        next.computeIfAbsent(s[0]) { Trie() }.add(s.drop(1))
    }

    fun addAll(strings: Iterable<CharSequence>) {
        strings.forEach(::add)
    }

    operator fun get(c: Char): Trie? = next[c]

    operator fun contains(s: CharSequence): Boolean =
        if (s.isEmpty()) isLeaf else next[s[0]]?.contains(s.drop(1)) ?: false
}

data class TrieMap<T>(
    private val next: MutableMap<Char, TrieMap<T>> = mutableMapOf(),
    private var leafValue: T? = null
) {
    fun put(s: CharSequence, value: T) {
        if (s.isEmpty()) {
            leafValue = value
            return
        }
        next.computeIfAbsent(s[0]) { TrieMap() }.put(s.drop(1), value)
    }

    operator fun set(s: CharSequence, value: T) = put(s, value)

    operator fun get(s: CharSequence): T? = if (s.isEmpty()) leafValue else next[s[0]]?.get(s.drop(1))

    operator fun get(c: Char): TrieMap<T>? = next[c]

    operator fun contains(s: String): Boolean = get(s) != null
}

/**
 * Follows the trie based on the [input] characters and calls [onEachValue] on each value found (restarting at the
 * top of the trie when this happens). The trie is assumed to contain a prefix code.
 */
inline fun <T> TrieMap<T>.decodePrefixStream(input: CharSequence, onEachValue: (T) -> Unit) {
    val current = StringBuilder()
    for (c in input) {
        current.append(c)
        val v = get(current)
        if (v != null) {
            onEachValue(v)
            current.clear()
        }
    }
    if (current.isNotEmpty()) {
        throw IncompleteEncodedStream(current.toString())
    }
}

/**
 * Follows the trie based on the [input] characters and returns the list of the decoded values (restarting at the
 * top of the trie when this happens). The trie is assumed to contain a prefix code.
 */
fun <T> TrieMap<T>.decodePrefixStream(input: String): List<T> {
    val result = mutableListOf<T>()
    decodePrefixStream(input) {
        result.add(it)
    }
    return result
}

class IncompleteEncodedStream(val remainingInput: String): Exception()
