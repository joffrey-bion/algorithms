package org.hildan.algorithms.math

import kotlin.math.*

fun Int.isPrime() = this > 1 && (2..sqrt(this.toDouble()).toInt()).none { this % it == 0}
// code golf versions of isPrime()
fun p(n:Int)=n>1&&(2..sqrt(n.toDouble()).toInt()).none{n%it==0}
// code golf versions of isPrime() (even smaller, but is an approx)
fun p(n:Long)=n.toBigInteger().isProbablePrime(10)

fun lcm(a: Int, b: Int): Int {
    if (a == 0 || b == 0) {
        return 0
    }
    val high = max(abs(a), abs(b))
    val low = min(abs(a), abs(b))
    var lcm = high
    while (lcm % low != 0) {
        lcm += high
    }
    return lcm
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

// GEOMETRY

fun dist(x1: Int, y1: Int, x2: Int, y2: Int) = hypot((x2 - x1).toDouble(), (y2 - y1).toDouble())
