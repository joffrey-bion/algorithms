package org.hildan.algorithms.strings

fun readInts(): List<Int> = readLine()!!.split(" ").map{ it.toInt() }
// code golf version
fun i()=readLine()!!.split(" ").map{it.toInt()}

// conversion from char to string necessary to avoid getting the ascii value of the char, and get the actual digit
fun String.digits(): List<Int> = map { "$it".toInt() }

fun String.asciiChars(): List<Int> = map { it.toInt() }

fun permutations(s: String): Set<String> {
    var perms = mutableSetOf<String>("")
    for (c in s) {
        perms = perms.flatMapTo(HashSet()) { it.insertEverywhere(c) }
    }
    return perms
}

private fun String.insertEverywhere(c: Char): Set<String> {
    val result = mutableSetOf<String>()
    for (i in 0..length) {
        result.add(substring(0, i) + c + substring(i))
    }
    return result
}
