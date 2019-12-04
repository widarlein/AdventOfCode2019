package day4

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Must provide input program")
        System.exit(0)
    }

    val (rangeStart, rangeStop) = args[0].split("-")

    val validPasswords = countValidPart1Passwords(rangeStart, rangeStop)
    println("Number of valid part1 passwords between $rangeStart-$rangeStop is $validPasswords")

    val validPart2Passwords = countValidPart2Passwords(rangeStart, rangeStop)
    println("Number of valid part2 passwords between $rangeStart-$rangeStop is $validPart2Passwords")
}

internal fun countValidPart1Passwords(start: String, stop: String): Int {
    var count = 0
    var next = selfOrNextNonDecreasing(start)
    while (next.toInt() <= stop.toInt()) {
        if (hasTwoAdjacentMatchingDigits(next)) {
            count++
        }
        next = selfOrNextNonDecreasing(next.inc())
    }

    return count
}

internal fun countValidPart2Passwords(start: String, stop: String): Int {
    var count = 0
    var next = selfOrNextNonDecreasing(start)
    while (next.toInt() <= stop.toInt()) {
        if (hasExactlyTwoAdjacentMatchingDigits(next)) {
            count++
        }
        next = selfOrNextNonDecreasing(next.inc())
    }

    return count
}

internal fun selfOrNextNonDecreasing(code: String): String {
    val index = indexOfDecreasingDigit(code)
    if (index == -1) return code

    return code.substring(0, index).padEnd(6, code[index - 1])
}

internal fun String.inc() = (this.toInt() + 1).toString()

private fun indexOfDecreasingDigit(code: String): Int {
    code.forEachIndexed { i, c ->
        if (i != 0) {
            if (c < code[i - 1]) {
                return i
            }
        }
    }
    return -1
}

internal fun hasExactlyTwoAdjacentMatchingDigits(code: String): Boolean {
    code.forEachIndexed { index, c ->
        if (index != 0) {
            if (index == 1) {
                if (c == code[index - 1] && c != code[index + 1]) {
                    return true
                }
            } else if (index == code.lastIndex) {
                if (c == code[index - 1] && c != code[index - 2]) {
                    return true
                }
            } else {
                if (c == code[index - 1] && c != code[index - 2] && c != code[index + 1]) {
                    return true
                }
            }

        }
    }
    return false
}

internal fun hasTwoAdjacentMatchingDigits(code: String): Boolean {
    code.forEachIndexed { index, c ->
        if (index != 0) {
            if (c == code[index - 1]) {
                return true
            }
        }
    }
    return false
}

internal fun countValidPart2PasswordsFail(start: String, stop: String): Int {
    var count = 0
    var next = selfOrNextNonDecreasing(start)
    next = makeNonMultiAdjacent(next)
    while (next.toInt() <= stop.toInt()) {
        if (hasTwoAdjacentMatchingDigits(next)) {
            count++
        }
        next = selfOrNextNonDecreasing(next.inc())
        next = makeNonMultiAdjacent(next)
    }

    return count
}

internal tailrec fun makeNonMultiAdjacent(code: String): String {
    val index = indexOfThirdAdjacentDigit(code)
    if (index == -1) return code

    var prefix = code.substring(0, index)
    val overflow = prefix.last() == '9' // "112999" gives prefix 11299
    if (overflow) {
        prefix = (prefix.toInt() + 1).toString() // New prefix 11300, this is decreasing and will always need to be fixed
    }

    // newPrefix.last() should never be '9'
    val paddedCode = prefix.padEnd(6, unsafeIncChar(prefix.last()))

    // get next non decreasing if overflow happened earlier
    return makeNonMultiAdjacent(if (overflow) selfOrNextNonDecreasing(paddedCode) else paddedCode)
}

// Only works if char is '0' - '8', hence unsafe.
internal fun unsafeIncChar(char: Char): Char {

    return (Character.getNumericValue(char) + 1 + 48).toChar()
}

internal fun indexOfThirdAdjacentDigit(code: String): Int {
    code.forEachIndexed { i, c ->
        if (i != 0 && i != 1) {
            if (c == code[i - 1] && c == code[i - 2]) {
                return i
            }
        }
    }
    return -1
}