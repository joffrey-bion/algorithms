package org.hildan.algorithms.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

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
