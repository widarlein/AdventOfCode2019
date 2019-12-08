package day7

fun main(args: Array<String>) {
    if (args.size < 1) {
        println("Must provide input program")
        System.exit(0)
    }

    val inputProgram = args[0].split(",").map { it.toInt() }.toIntArray()

    val highestOutput = bruteForceThrusters(inputProgram)
    println("Highest part 1 output found: $highestOutput")

    val highestPart2Output = bruteForcePart2Thrusters(inputProgram)
    println("Highest part 2 output found: $highestPart2Output")
}

internal fun bruteForceThrusters(thrusterProgram: IntArray): Int {

    val permutations = getAllPhasePermutations()

    var highestOutput = 0
    permutations.forEach {permutation ->

        var lastOutput = listOf(0)
        for (amplifier in 0..4) {
            lastOutput = runProgram(thrusterProgram.copyOf(), listOf(permutation[amplifier], lastOutput.first()))
        }
        if (lastOutput.first() > highestOutput) {
            highestOutput = lastOutput.first()
        }
    }
    return highestOutput
}

internal fun bruteForcePart2Thrusters(thrusterProgram: IntArray): Int {

    val permutations = getPart2PhasePermutations()

    var highestOutput = 0
    permutations.forEach {permutation ->

        val amplifiers = permutation.map { phaseSetting -> Amplifier(thrusterProgram.copyOf(), phaseSetting) }

        var amp4Output = 0

        amplifiers[0].onOutput = { output -> amplifiers[1].addInput(output)}
        amplifiers[1].onOutput = { output -> amplifiers[2].addInput(output)}
        amplifiers[2].onOutput = { output -> amplifiers[3].addInput(output)}
        amplifiers[3].onOutput = { output -> amplifiers[4].addInput(output)}
        amplifiers[4].onOutput = { output ->
            println("LAST AMP OUTPUT")
            amp4Output = output
            if (amplifiers[4].isRunning) {
                amplifiers[0].addInput(output)
            }
        }

        amplifiers.forEach { it.run() }

        amplifiers[0].addInput(0)

        while (amplifiers.any { it.isRunning }) {
            Thread.sleep(2)
        }
        if (amp4Output > highestOutput) {
            highestOutput = amp4Output
        }
    }
    return highestOutput
}

internal fun getAllPhasePermutations(): MutableList<IntArray> {
    val permutations = mutableListOf<IntArray>()
    for (a in 0..4) {
        for (b in 0..4) {
            if (b == a) continue
            for (c in 0..4) {
                if (c == a || c == b) continue
                for (d in 0..4) {
                    if (d == a || d == b || d == c) continue
                    for (e in 0..4) {
                        if (e == a || e == b || e == c || e == d) continue
                        permutations.add(intArrayOf(a, b, c, d, e))
                    }
                }
            }
        }
    }
    return permutations
}

internal fun getPart2PhasePermutations(): MutableList<IntArray> {
    val permutations = mutableListOf<IntArray>()
    for (a in 5..9) {
        for (b in 5..9) {
            if (b == a) continue
            for (c in 5..9) {
                if (c == a || c == b) continue
                for (d in 5..9) {
                    if (d == a || d == b || d == c) continue
                    for (e in 5..9) {
                        if (e == a || e == b || e == c || e == d) continue
                        permutations.add(intArrayOf(a, b, c, d, e))
                    }
                }
            }
        }
    }
    return permutations
}

internal fun runProgram(memory: IntArray, inputs: List<Int>): MutableList<Int> {
    val mutableInputs = inputs.toMutableList()
    val mutableOutputs = mutableListOf<Int>()
    var pointer = 0
    while (memory[pointer] != 99 || pointer > memory.size) {
        val (opcode, parameterModes) = memory[pointer].toOpCodeAndParameterModes()

        debug(opcode, parameterModes, memory, pointer)
        pointer = when(opcode) {
            1 -> add(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
            2 -> multiply(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
            3 -> storeInput(pointer + 1, readNextInput(mutableInputs), memory)
            4 -> output(pointer + 1, memory, parameterModes, mutableOutputs)
            5 -> jumpIfTrue(pointer +1, pointer +2, memory, parameterModes)
            6 -> jumpIfFalse(pointer +1, pointer +2, memory, parameterModes)
            7 -> lessThan(pointer +1, pointer +2, pointer +3, memory, parameterModes)
            8 -> equals(pointer +1, pointer +2, pointer +3, memory, parameterModes)
            else -> throw IllegalArgumentException("Unsupported Opcode found at position $pointer: $opcode")
        }
    }

    if (pointer > memory.size) {
        println("Program ran out without exit opcode 99.")
    }

    //println("Execution ended. Program state:\n${memory.joinToString(",")}")
    return mutableOutputs
}

internal fun add(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (first, second) = parameterValues(parameterModes, memory, memory[firstPos], memory[secondPos])
    memory[memory[resultPos]] = first + second
    return firstPos - 1 + 4 // hack since we know pointer is firstPost - 1
}

internal fun multiply(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (first, second) = parameterValues(parameterModes, memory, memory[firstPos], memory[secondPos])
    memory[memory[resultPos]] = first * second
    return firstPos - 1 + 4 // hack since we know pointer is firstPost - 1
}

internal fun storeInput(storePos: Int, input: Int, memory: IntArray): Int {
    memory[memory[storePos]] = input
    return storePos - 1 + 2 // hack since we know pointer is storePos - 1
}

internal fun output(
    readPos: Int,
    memory: IntArray,
    parameterModes: Int,
    mutableOutputs: MutableList<Int>
): Int {
    val (first) = parameterValues(parameterModes, memory, memory[readPos])
    mutableOutputs.add(first)
    println(first)
    return readPos - 1 + 2 // hack since we know pointer is storePos - 1
}

internal fun jumpIfTrue(guardPos: Int, pointerValPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (guard, pointerVal) = parameterValues(parameterModes, memory, memory[guardPos], memory[pointerValPos])
    if (guard != 0) {
        return pointerVal
    }
    return guardPos - 1 + 3 // hack since we know pointer is guardPos - 1
}

internal fun jumpIfFalse(guardPos: Int, pointerValPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (guard, pointerVal) = parameterValues(parameterModes, memory, memory[guardPos], memory[pointerValPos])
    if (guard == 0) {
        return pointerVal
    }
    return guardPos - 1 + 3 // hack since we know pointer is guardPos - 1
}

internal fun lessThan(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (first, second) = parameterValues(parameterModes, memory, memory[firstPos], memory[secondPos])

    memory[memory[resultPos]] = if (first < second) 1 else 0

    return firstPos - 1 + 4 // hack since we know pointer is firstPos - 1
}

internal fun equals(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (first, second) = parameterValues(parameterModes, memory, memory[firstPos], memory[secondPos])

    memory[memory[resultPos]] = if (first == second) 1 else 0

    return firstPos - 1 + 4 // hack since we know pointer is firstPos - 1
}

internal fun parameterValues(parameterModes: Int, memory: IntArray, vararg parameters: Int): List<Int> {
    var paramModes = parameterModes
    return parameters.map {
        val mode = paramModes % 10
        paramModes /= 10
        when (mode) {
            0 -> memory[it]
            1 -> it
            else -> throw IllegalArgumentException("No such parameter mode $it")
        }
    }
}

internal fun readNextInput(inputs: MutableList<Int>): Int {
    val input = inputs.first()
    inputs.removeAt(0)
    return input
}

internal fun requestInput(): Int {
    var input: Int? = null
    do {
        print("Enter input: ")
        val inputText = readLine()!!
        try {
            input = inputText.toInt()
        } catch (e: NumberFormatException) {
            println("Illegal input format. Input should be an integer")
            continue
        }
    } while (input == null)
    return input
}

internal fun Int.toOpCodeAndParameterModes(): Pair<Int, Int> {
    val opcode = this % 100
    val parameterModes = this / 100
    return opcode to parameterModes
}