package day1

fun main(args: Array<String>) {
    if (args.size < 1) {
        println("Must provide input set")
        System.exit(0)
    }

    val fuelSum = naiveFuelCalc(args[0])
    println("Part 1 amount of fuel: $fuelSum")

    val smartFuelSum = smartFuelCalc(args[0])
    println("Part 2 amount of fuel: $smartFuelSum")
}

private fun naiveFuelCalc(input: String) = input.lineSequence()
        .map { it.toInt() }
        .map { Math.floor(it/3.0) - 2 }
        .sum()

private fun smartFuelCalc(input: String) = input.lineSequence()
        .map { it.toInt() }
        .map { calculateFuelForMass(it) }
        .sum()

private fun calculateFuelForMass(mass: Int): Int {
    val fuel = (Math.floor(mass/3.0) - 2).toInt()
    if (fuel <= 0) {
        return 0
    } else {
        return fuel + calculateFuelForMass(fuel)
    }
} 