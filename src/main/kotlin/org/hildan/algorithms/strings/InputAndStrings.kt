package org.hildan.algorithms.strings

fun readInts(): List<Int> = readLine()!!.split(" ").map{ it.toInt() }
// code golf version
fun i()=readLine()!!.split(" ").map{it.toInt()}

// conversion from char to string necessary to avoid getting the ascii value of the char, and get the actual digit
fun String.digits(): List<Int> = map { "$it".toInt() }

fun String.asciiChars(): List<Int> = map { it.toInt() }
