package day4

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AocKtTest {

    @Test
    fun nextNonDecreasing() {
        assertTrue(selfOrNextNonDecreasing("123456") == "123456")
        assertTrue(selfOrNextNonDecreasing("123546") == "123555")
    }

    @Test
    fun `123455 is adjacent`() {
        assertTrue(hasTwoAdjacentMatchingDigits("123455"))
    }

    @Test
    fun `123456 is not adjacent`() {
        assertFalse(hasTwoAdjacentMatchingDigits("123456"))
    }

    @Test
    fun `111111-111112 has two valid passwords part1`() {
        assertEquals(2, countValidPart1Passwords("111111", "111112"))
    }

    @Test
    fun `111119-111122 has two valid passwords part1`() {
        assertEquals(2, countValidPart1Passwords("111119", "111122"))
    }

    @Test
    fun `112233 has exactly 2 adjacent chars`() {
        assertTrue(hasExactlyTwoAdjacentMatchingDigits("112233"))
    }

    @Test
    fun `123444 doesn't have exactly 2 adjacent chars`() {
        assertFalse(hasExactlyTwoAdjacentMatchingDigits("123444"))
    }

    @Test
    fun `111122 has exactly 2 adjacent chars`() {
        assertTrue(hasExactlyTwoAdjacentMatchingDigits("111122"))
    }

    /// FAILED TESTS FROM MISREAD CRITERIA. (works, but does not follow the correct rules, see README.md)
    @Test
    fun `112233 is not multi-adjacent`() {
        assertEquals("112233", makeNonMultiAdjacent("112233"))
    }

    @Test
    fun `123333 makes 123344`() {
        assertEquals("123344", makeNonMultiAdjacent("123333"))
    }

    @Test
    fun `122222 makes 122334`() {
        assertEquals("122334", makeNonMultiAdjacent("122222"))
    }

    @Test
    fun `112999 makes 113344`() {
        assertEquals("113344", makeNonMultiAdjacent("112999"))
    }

    @Test
    fun `199999 makes 223344`() {
        assertEquals("223344", makeNonMultiAdjacent("199999"))
    }

    @Test
    fun `111119-111122 has no valid passwords part2`() {
        assertEquals(0, countValidPart2PasswordsFail("111119", "111122"))
    }

    @Test
    fun `199999-223345 has 2 valid passwords part2`() {
        assertEquals(2, countValidPart2PasswordsFail("199999", "223345"))
    }
}