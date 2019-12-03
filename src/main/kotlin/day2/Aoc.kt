package day2

fun main(args: Array<String>) {
    if (args.size < 1) {
        println("Must provide input program")
        System.exit(0)
    }

    val inputProgram = args[0].split(",").map { it.toInt() }.toIntArray()

    val program = inputProgram.clone()
    val output = runProgram(program)
    println("Part 1 output: $output")

    val code = findNounAndVerb(19690720, inputProgram.clone())
    println("Part 2 code: $code")
}

/*
 * Finds the first pair of noun and verb to produce an expected output from given program memory
 */
private fun findNounAndVerb(expectedOutput: Int, initMemory: IntArray): Int {
    // loop over all possible nouns and verbs
    for (noun in (0..99)) {
        for (verb in (0..99)) {
            val memory = initMemory.clone()
            memory[1] = noun
            memory[2] = verb
            val output = runProgram(memory)
            if (output == expectedOutput) {
                return 100 * noun + verb
            }
        }
    }
    // no pair of noun and verb found to produce output
    return -1
}

private fun runProgram(memory: IntArray): Int {
    var pointer = 0
    while (memory[pointer] != 99 || pointer > memory.size) {
        when(memory[pointer]) {
            1 -> add(pointer + 1, pointer + 2, pointer + 3, memory)
            2 -> multiply(pointer + 1, pointer + 2, pointer + 3, memory)
            else -> println("Unsupported Opcode found at position $pointer: ${memory[pointer]}")
        }
        pointer += 4
    }

    if (pointer > memory.size) {
        println("Program ran out without exit opcode 99.")
    }

    //println("Execution ended. Program state:\n${memory.joinToString(",")}")
    return memory[0]
}

private fun add(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray) {
    memory[memory[resultPos]] = memory[memory[firstPos]] + memory[memory[secondPos]]
}

private fun multiply(firstPos: Int, secondPos: Int, resultPos: Int, memory: IntArray) {
    memory[memory[resultPos]] = memory[memory[firstPos]] * memory[memory[secondPos]]
}
