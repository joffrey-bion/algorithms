package org.hildan.algorithms.math

import java.util.Objects
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.round
import kotlin.random.Random

data class Point(val x: Double, val y: Double) {

    override fun toString() = "($x, $y)"

    // The data-class-generated equals() uses java.lang.Double.compare() under the hood, which thinks 0.0 > -0.0
    // https://kotlinlang.org/docs/reference/basic-types.html#floating-point-numbers-comparison
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

    override fun hashCode(): Int = Objects.hash(x, y)
}

typealias Segment = Pair<Point, Point>

/**
 * Defines a line by the equation "ax + by = c".
 * Note that a and b cannot both be 0.
 */
data class Line(
    val a: Double,
    val b: Double,
    val c: Double
) {
    init {
        require(a != .0 || b != .0) { "a and b cannot both be 0 (equation 0x + 0y = $c (c) is not a line)" }
    }

    val slope: Double get() = if (b == .0) Double.POSITIVE_INFINITY else -a/b

    override fun toString(): String = "$a * x + $b * y = $c"

    companion object {
        fun verticalThrough(point: Point) = Line(1.0, 0.0, point.x)
        fun horizontalThrough(point: Point) = Line(0.0, 1.0, point.y)
        fun diagonalAscendingThrough(point: Point) = Line(1.0, -1.0, point.x - point.y)
        fun diagonalDescendingThrough(point: Point) = Line(1.0, 1.0, point.x + point.y)
    }
}

fun Point.distanceTo(other: Point) = hypot(x - other.x, y - other.y)

fun Point.rounded() = Point(round(x), round(y))

fun Point.cropTo(width: Double, height: Double) = Point(
    x.coerceIn(.0, width),
    y.coerceIn(.0, height)
)

fun Point.reflection(center: Point) = Point(2 * center.x - x, 2 * center.y - y)

// From the following link (but with an opposite C because of the equation form we use: ax + by = c)
// http://www.sdmath.com/math/geometry/reflection_across_line.html#formulasabc
fun Point.reflection(axis: Line) = with(axis) {
    Point(
        x = ((b * b - a * a) * x - 2 * a * b * y + 2 * a * c) / (a * a + b * b),
        y = ((a * a - b * b) * y - 2 * a * b * x + 2 * b * c) / (a * a + b * b)
    )
}

fun Point.reflectionHorizontal(verticalAxisGuide: Point) = Point(2 * verticalAxisGuide.x - x, y)

fun Point.reflectionVertical(horizontalAxisGuide: Point) = Point(x, 2 * horizontalAxisGuide.y - y)

fun Iterable<Point>.centroid() = Point(map { it.x }.average(), map { it.y }.average())

fun Segment.length(): Double = first.distanceTo(second)

fun Segment.midPoint(): Point {
    val (x1, y1) = this.first
    val (x2, y2) = this.second
    return Point((x1 + x2) / 2, (y1 + y2) / 2)
}

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

/**
 * Returns the intersection point, or null if both lines are parallel.
 */
fun Line.intercept(line: Line): Point? {
    if (isParallelTo(line)) {
        return null
    }
    val (a1, b1, c1) = this
    val (a2, b2, c2) = line
    val x = (b1 * c2 - b2 * c1) / (b1 * a2 - b2 * a1)
    val y = (a2 * c1 - a1 * c2) / (b1 * a2 - b2 * a1)
    return Point(x, y)
}

fun Segment.intercept(line: Line): Point? {
    if ((first > line && second > line) || (first < line && second < line)) {
        return null // the segment doesn't cross the line, both points are on the same side of it
    }
    return toLine().intercept(line)
}

fun Line.isParallelTo(line: Line): Boolean = slope == line.slope

fun Segment.isParallelTo(line: Line): Boolean = toLine().isParallelTo(line)

// vertices rotating counter-clockwise, same for segments
data class Polygon(
    val vertices: List<Point>
) {
    init {
        require(vertices.size >= 3) { "A polygon must have at least 3 vertices, got ${vertices.size}: $vertices" }
    }

    val width: Double
        get() = vertices.map { it.x }.let { it.max()!! - it.min()!! }
    val height: Double
        get() = vertices.map { it.y }.let { it.max()!! - it.min()!! }

    /** Thickness from bottom-left to top-right */
    val ascendingDiagonalThickness: Double
        get() = vertices.map { it.x + it.y }.let { it.max()!! - it.min()!! }
    /** Thickness from top-left to bottom-right */
    val descendingDiagonalThickness: Double
        get() = vertices.map { it.x - it.y }.let { it.max()!! - it.min()!! }

    fun area(): Double {
        val wrappingPairs = vertices.zipWithNext() + (vertices.last() to vertices.first())
        return abs(wrappingPairs.fold(.0) { a, (p1, p2) -> a + (p1.x * p2.y - p1.y * p2.x) } / 2)
    }

    fun centroid() = Point(vertices.map { it.x }.average(), vertices.map { it.y }.average())

    fun cut(line: Line, sideToKeep: Side): Polygon {
        val keptVertices = mutableListOf<Point>()
        for (i in vertices.indices) {
            val p = this[i]
            if (p.onWantedSideOf(line, sideToKeep)) {
                val prev = this[i - 1]
                if (!prev.onWantedSideOf(line, sideToKeep)) {
                    val cutPoint = (prev to p).intercept(line)!!
                    keptVertices.addIfDifferentFromLast(cutPoint)
                }
                keptVertices.addIfDifferentFromLast(p)
            } else {
                val prev = this[i - 1]
                if (prev.onWantedSideOf(line, sideToKeep)) {
                    val cutPoint = (prev to p).intercept(line)!!
                    keptVertices.addIfDifferentFromLast(cutPoint)
                }
            }
        }
        return Polygon(keptVertices)
    }

    private fun MutableList<Point>.addIfDifferentFromLast(p: Point) {
        if (p != lastOrNull()) {
            add(p)
        }
    }

    private fun Point.onWantedSideOf(line: Line, sideToKeep: Side) = when (sideToKeep) {
        Side.ABOVE -> this >= line
        Side.BELOW -> this <= line
    }

    private operator fun get(i: Int) = vertices[(i + vertices.size) % vertices.size]
}

enum class Side {
    /** A point (x0,y0) is considered "above" a line ax+by=c if ax0 + by0 > c */
    ABOVE,
    /** A point (x0,y0) is considered "below" a line ax+by=c if ax0 + by0 < c */
    BELOW
}

operator fun Line.compareTo(p: Point) = c.compareTo(a * p.x + b * p.y)

operator fun Point.compareTo(line: Line) = (line.a * x + line.b * y).compareTo(line.c)
