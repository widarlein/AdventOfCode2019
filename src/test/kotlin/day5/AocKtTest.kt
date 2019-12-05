package day5

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AocKtTest {

    @Test
    fun add() {
        val memory = intArrayOf(1002,4,3,4,96)
        add(1,2, 3, memory, 10)
        assertTrue(intArrayOf(1002,4,3,4,99).contentEquals(memory)) {
            "${memory.contentToString()}, ${intArrayOf(1002,4,3,4,99).contentToString()}"
        }
    }

    @Test
    fun multiply() {
        val memory = intArrayOf(1002,4,3,4,33)
        multiply(1,2, 3, memory, 10)
        assertTrue(intArrayOf(1002,4,3,4,99).contentEquals(memory))
    }

    @Test
    fun `store 99 at position 3`() {
        val memory = intArrayOf(3,2,666)
        storeInput(1, 99, memory)
        assertTrue(intArrayOf(3,2,99).contentEquals(memory))
    }

    @Test
    fun `run program 1002,4,3,4,33`() {
        val memory = intArrayOf(1002,4,3,4,33)
        runProgram(memory)
        assertTrue(intArrayOf(1002,4,3,4,99).contentEquals(memory))
    }

//    @Test
//    fun `run program 3,4,4,4,33`() {
//        println("Test")
//        val memory = intArrayOf(3,4,4,4,33)
//        runProgram(memory)
//    }
}