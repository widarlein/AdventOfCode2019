package day7

import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.thread

class Amplifier(program: IntArray, phaseSetting: Int) {

    var isRunning = false
    var onOutput: (Int) -> Unit = {}

    private val memory = program
    private val inputQueue = LinkedBlockingDeque<Int>()

    init {
        inputQueue.offer(phaseSetting)
    }

    fun run() {
        isRunning = true
        thread {
            runProgram()
            println("Amp $this ended execution")
            isRunning = false
        }
    }

    fun addInput(input: Int) {
        println("Amp $this received input")
        inputQueue.offer(input)
    }

    private fun runProgram() {
        var pointer = 0
        while (memory[pointer] != 99 || pointer > memory.size) {
            val (opcode, parameterModes) = memory[pointer].toOpCodeAndParameterModes()

            debug(opcode, parameterModes, memory, pointer)
            pointer = when(opcode) {
                1 -> add(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
                2 -> multiply(pointer + 1, pointer + 2, pointer + 3, memory, parameterModes)
                3 -> storeInput(pointer + 1, readInput(), memory)
                4 -> this.output(pointer + 1, memory, parameterModes)
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

    }

    private fun readInput(): Int {
        println("Amp $this awaiting input")
        return inputQueue.take()
    }

    private fun output(
        readPos: Int,
        memory: IntArray,
        parameterModes: Int
    ): Int {
        val (first) = parameterValues(parameterModes, memory, memory[readPos])
        println("Amp $this outputs $first")
        onOutput(first)

        return readPos - 1 + 2 // hack since we know pointer is storePos - 1
    }
}