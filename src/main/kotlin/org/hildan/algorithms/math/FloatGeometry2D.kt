package org.hildan.algorithms.math

import java.util.Objects
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.round

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

    companion object {
        val ORIGIN = Point(0.0, 0.0)
    }
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

    /** [Double.POSITIVE_INFINITY] if this line is vertical. */
    val slope: Double get() = if (b == .0) Double.POSITIVE_INFINITY else -a/b

    fun isParallelTo(line: Line): Boolean = (b == .0 && line.b == .0) || slope == line.slope

    override fun toString(): String = "$a * x + $b * y = $c"

    companion object {
        fun of(p1: Point, p2: Point): Line = Line(
            a = p1.y - p2.y,
            b = p2.x - p1.x,
            c = p2.x * p1.y - p1.x * p2.y
        )
        fun verticalThrough(point: Point) = Line(1.0, 0.0, point.x)
        fun horizontalThrough(point: Point) = Line(0.0, 1.0, point.y)
        fun diagonalAscendingThrough(point: Point) = Line(1.0, -1.0, point.x - point.y)
        fun diagonalDescendingThrough(point: Point) = Line(1.0, 1.0, point.x + point.y)
    }
}

data class Ray(val start: Point, val target: Point)

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

fun Segment.toLine(): Line = Line.of(first, second)

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
    // FIXME what about when both lines are the same line?
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

fun Segment.intercept(ray: Ray): Point? {
    TODO("ray interception not implemented")
    // Note that we should consider when the ray overlaps completely with this segment (parallel + ON it)
    // Should we return the segment as intersection? throw an exception?
}

fun Segment.isParallelTo(line: Line): Boolean = toLine().isParallelTo(line)

fun Point.rayTowards(point: Point): Ray {
    if (this == point) {
        throw IllegalArgumentException("Cannot define a half-line from 2 identical points")
    }
    return Ray(this, point)
}

// vertices rotating counter-clockwise, same for segments
data class Polygon(
    val vertices: List<Point>
) {
    init {
        require(vertices.size >= 3) { "A polygon must have at least 3 vertices, got ${vertices.size}: $vertices" }
    }

    val edges: List<Segment>
        get() = vertices.zipWithNext() + (vertices.last() to vertices.first())

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
        return abs(edges.fold(.0) { a, (p1, p2) -> a + (p1.x * p2.y - p1.y * p2.x) } / 2)
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

    fun intercept(line: Line): List<Point> = edges.mapNotNull { it.intercept(line) }

    operator fun contains(point: Point): Boolean {
        val ray = point.rayTowards(Point.ORIGIN)
        // FIXME we should also consider when the ray is EXACTLY going along a whole edge, overlapping completely
        val intersectionPoints = edges.mapNotNull {
            // FIXME when the ray intercepts the polygon exactly ON one of the vertices, we should count it only once
            //  (not for both edges) when actually going in or out of the polygon, but twice when staying on the same side.
            //  One way to do that is to count the point only if the other end of the edge is on a given "side" of the ray.
            //  This way if both edges are on the same side we count them an even number of times, and if there is an edge
            //  on each side we count it exactly once. This leads to the desired behaviour. It is as if the ray was slightly
            //  above (or below) the vertex, depending on the side we choose, so we avoid the special case.
            it.intercept(ray)
        }
        return intersectionPoints.size % 2 == 1
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
