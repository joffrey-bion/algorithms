package org.hildan.algorithms.math

import javax.sound.sampled.LineListener
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

data class Point(val x: Double, val y: Double) {
    override fun toString() = "${x.roundToInt()} ${y.roundToInt()}"
}

fun Point.distanceTo(other: Point) = hypot(x - other.x, y - other.y)

fun List<Point>.centroid() = Point(map { it.x }.average(), map { it.y }.average())

fun Point.reflection(center: Point) = Point(2 * center.x - x, 2 * center.y - y)

fun Point.cropTo(width: Int, height: Int) = Point(
    x.coerceAtLeast(.0).coerceAtMost(width - 1.0),
    y.coerceAtLeast(.0).coerceAtMost(height - 1.0)
)

typealias Segment = Pair<Point, Point>

// ax + by = c
data class Line(
    val a: Double,
    val b: Double,
    val c: Double
) {
    init {
        require(a != .0 || b != .0) { "a and b cannot both be 0 (equation c = 0 is not a line)" }
    }
}

operator fun Line.compareTo(p: Point) = c.compareTo(a * p.x + b * p.y)

operator fun Point.compareTo(line: Line) = (line.a * x + line.b * y).compareTo(line.c)

fun Segment.toLine(): Line {
    val (x1, y1) = this.first
    val (x2, y2) = this.second
    return Line(
        a = y1 - y2,
        b = x2 - x1,
        c = x2 * y1 - x1 * y2
    )
}

fun Segment.bisector(): Line {
    val (x1, y1) = this.first
    val (x2, y2) = this.second
    return Line(
        a = x2 - x1,
        b = y2 - y1,
        c = (x2 * x2 + y2 * y2 - x1 * x1 - y1 * y1) / 2
    )
}

fun Segment.midPoint(): Point {
    val (x1, y1) = this.first
    val (x2, y2) = this.second
    return Point((x1 + x2) / 2, (y1 + y2) / 2)
}

fun Line.intercept(line: Line): Point {
    val (a1, b1, c1) = this
    val (a2, b2, c2) = line
    val x = (b1 * c2 - b2 * c1) / (b1 * a2 - b2 * a1)
    val y = (a2 * c1 - a1 * c2) / (b1 * a2 - b2 * a1)
    return Point(x, y)
}

fun Segment.intercept(line: Line): Point = toLine().intercept(line)

// vertices rotating counter-clockwise, same for segments
data class Polygon(
    val vertices: List<Point>
) {
    fun cutBottom(line: Line): Polygon = cut(line, keepUpperPart = true)

    fun cutTop(line: Line): Polygon = cut(line, keepUpperPart = false)

    private fun cut(line: Line, keepUpperPart: Boolean): Polygon {
        val keptVertices = mutableListOf<Point>()
        for (i in vertices.indices) {
            val p = this[i]
            if (p.onGoodSideOf(line, keepUpperPart)) {
                val prev = this[i-1]
                if (!prev.onGoodSideOf(line, keepUpperPart)) {
                    val cutPoint = (prev to p).intercept(line)
                    if (cutPoint != p) {
                        keptVertices.add(cutPoint)
                    }
                }
                keptVertices.add(p)
            } else {
                val prev = this[i-1]
                if (prev.onGoodSideOf(line, keepUpperPart)) {
                    val cutPoint = (prev to p).intercept(line)
                    keptVertices.add(cutPoint)
                }
            }
        }
        return Polygon(keptVertices)
    }

    private fun Point.onGoodSideOf(line: Line, upperIsGood: Boolean) = if (upperIsGood) this >= line else this <= line

    private operator fun get(i: Int) = vertices[(i + vertices.size) % vertices.size]

    fun area(): Double {
        val wrappingPairs = vertices.zipWithNext() + (vertices.last() to vertices.first())
        return abs(wrappingPairs.fold(.0) { a, (p1, p2) -> a + (p1.x * p2.y - p1.y * p2.x) } / 2)
    }

    fun centroid() = Point(vertices.map { it.x }.average(), vertices.map { it.y }.average())
}
