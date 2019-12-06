package day6

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Must provide input orbits")
        System.exit(0)
    }

    val inputLines = File("src/main/kotlin/day6/${args[0]}").readLines()

    val objects = buildGraph(inputLines)

    val numberOfOrbits = objects.sumBy { countOrbits(it) }

    println("Part1: number of orbits is $numberOfOrbits")

    dfs(objects.find { it.name == "YOU" }?.orbits!!, objects.find { it.name == "SAN" }?.orbits!!)
}

private fun buildGraph(inputLines: List<String>): MutableSet<OrbitObject> {
    val objectSet = mutableSetOf<OrbitObject>()
    inputLines.forEach {
        val (pivotName, orbitalName) = it.split(")")

        var pivot = objectSet.find { it.name == pivotName }
        var orbital = objectSet.find { it.name == orbitalName }

        if (pivot == null) {
            pivot = OrbitObject(pivotName, null)
            objectSet.add(pivot)
        }

        if (orbital == null) {
            orbital = OrbitObject(orbitalName, pivot)
            objectSet.add(orbital)
        } else {
            orbital.orbits = pivot
        }

        pivot.orbitals.add(orbital)
    }
    return objectSet
}

internal fun dfs(from: OrbitObject, to: OrbitObject, visited: MutableSet<OrbitObject> = mutableSetOf(), steps: Int = 0) {

    // YE GODS THIS TOOK A LONG TIME TO DO SINCE I WANTED TO MAKE FUNCTION WITH A RETURN VALUE
    // BUT IN THE END I GAVE UP. FRUSTRATED.

    if (from == to) {
        println("We are here at $steps")
    }

    visited.add(from)
    val nexts = (from.orbitals + from.orbits).filterNotNull().filterNot { visited.contains(it) }
    nexts.forEach { dfs(it, to, visited, steps + 1) }
}

internal fun countOrbits(orbitObject: OrbitObject): Int {
    val orbits = orbitObject.orbits ?: return 0
    return 1 + countOrbits(orbits)
}

class OrbitObject(val name: String, var orbits: OrbitObject? = null, vararg orbitals: OrbitObject) {

    val orbitals: MutableList<OrbitObject> = orbitals.toMutableList()

    override fun equals(other: Any?): Boolean {
        return other != null && other is OrbitObject && name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}