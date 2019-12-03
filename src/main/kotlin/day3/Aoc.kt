package day3

import java.io.File
import kotlin.IllegalArgumentException

fun main(args: Array<String>) {
    if (args.size < 1) {
        println("Must provide input data")
        System.exit(0)
    }
    val inputLines = File("src/main/kotlin/day3/${args[0]}").readLines()

    val wiresDirections = inputLines.map { it.split(",") }
    val wiresCoordinates = wiresDirections.map { getWireCoorinates(it).drop(1)}

    val intersections = findWireIntersections(wiresCoordinates[0], wiresCoordinates[1])

    val closestIntersection = intersections.minBy { it.distance }
    println("Part 1. Distance to closest intersection($closestIntersection): ${closestIntersection?.distance}")

    val closestIntersectionAndSteps = findIntersectionWithLeastSteps(intersections, wiresCoordinates)
    println("Part 2. Fewest steps to intersection ${closestIntersectionAndSteps?.first} with ${closestIntersectionAndSteps?.second} steps")
}

private fun getWireCoorinates(wireDirections: List<String>): List<Pair<Int, Int>> {
     return wireDirections.fold(listOf(0 to 0)) { sum, move ->
        val lastCoordinate = sum.last()
        val moveCoords = when (move.direction()) {
            'R' -> right(move.steps() from lastCoordinate)
            'L' -> left(move.steps() from lastCoordinate)
            'U' -> up(move.steps() from lastCoordinate)
            'D' -> down(move.steps() from lastCoordinate)
            else -> throw IllegalArgumentException("NO SUCH DIRECTION: ${move.direction()}")
        }
        sum + moveCoords
    }
}

private fun findIntersectionWithLeastSteps(intersections: List<Pair<Int, Int>>, wiresCoordinates: List<List<Pair<Int, Int>>>): Pair<Pair<Int, Int>, Int>? {
    return intersections.map { it to stepsToIntersectionForWire(it, wiresCoordinates[0]) + stepsToIntersectionForWire(it, wiresCoordinates[1]) }
        .minBy { it.second }
}

private fun stepsToIntersectionForWire(intersection: Pair<Int, Int>, wireCoordinates: List<Pair<Int, Int>>) = wireCoordinates.indexOf(intersection) + 1

private fun right(stepsFromPoint: Pair<Int, Pair<Int, Int>>): List<Pair<Int, Int>> {
    val (steps, point) = stepsFromPoint
    val x = point.first
    val y = point.second
    return (x+1..x+steps).map { it to y }
}

private fun left(stepsFromPoint: Pair<Int, Pair<Int, Int>>): List<Pair<Int, Int>> {
    val (steps, point) = stepsFromPoint
    val x = point.first
    val y = point.second
    return (x-1 downTo x-steps).map { it to y }
}

private fun up(stepsFromPoint: Pair<Int, Pair<Int, Int>>): List<Pair<Int, Int>> {
    val (steps, point) = stepsFromPoint
    val x = point.first
    val y = point.second
    return (y+1..y+steps).map { x to it }
}

private fun down(stepsFromPoint: Pair<Int, Pair<Int, Int>>): List<Pair<Int, Int>> {
    val (steps, point) = stepsFromPoint
    val x = point.first
    val y = point.second
    return (y-1 downTo y-steps).map { x to it }
}

private fun findWireIntersections(wire1: List<Pair<Int, Int>>, wire2: List<Pair<Int, Int>>) = wire1.filter { wire2.contains(it) }


private infix fun Int.from(point: Pair<Int, Int>) = this to point

private fun String.direction() = this[0]

private fun String.steps() = this.substring(1).toInt()

val Pair<Int, Int>.distance: Int
        get() = Math.abs(this.first) + Math.abs(this.second)

private infix fun <A, B, C> Pair<A, B>.and(third: C): Triple<A, B, C> = Triple(this.first, this.second, third)