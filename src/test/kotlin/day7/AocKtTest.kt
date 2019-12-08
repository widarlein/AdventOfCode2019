package day7

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AocKtTest {

    @Test
    fun `check permutations are 5*4*3*2*1`() {
        val permutations = getAllPhasePermutations()
        assertEquals(5*4*3*2*1, permutations.size)
    }

    @Test
    fun `highest output is 43210`() {
        val program = intArrayOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)
        assertEquals(43210, bruteForceThrusters(program))
    }


    @Test
    fun `part 2 output is 43210`() {
        val program = intArrayOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)
        assertEquals(139629729, bruteForcePart2Thrusters(program))
    }
}