package day5

fun main(args: Array<String>) {
    if (args.size < 1) {
        println("Must provide input program")
        System.exit(0)
    }

    val inputProgram = args[0].split(",").map { it.toInt() }.toIntArray()

    val program = inputProgram.clone()
    val output = runProgram(program)

}

internal fun runProgram(memory: IntArray): Int {
    var pointer = 0
    while (memory[pointer] != 99 || pointer > memory.size) {
        val (opcode, parameterModes) = memory[pointer].toOpCodeAndParameterModes()

        debug(opcode, parameterModes, memory, pointer)
        pointer = when(opcode) {
            1 -> add(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
            2 -> multiply(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
            3 -> storeInput(pointer + 1, requestInput(), memory)
            4 -> output(pointer + 1, memory, parameterModes)
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
    return memory[0]
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

internal fun output(readPos: Int, memory: IntArray, parameterModes: Int): Int {
    val (first) = parameterValues(parameterModes, memory, memory[readPos])
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