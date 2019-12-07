package day5

var DEBUG = true

fun debug(opcode: Int, parameterMode: Int, memory: IntArray, pointer: Int) {
    if (DEBUG) {
        println("Will execute OPCODE $opcode with parameter modes $parameterMode\n${memory.copyOfRange(pointer, pointer + 4).contentToString()}")
        print("Press c to continue or q to quit: ")
        do {
            val input = readLine()!!
            if (input == "q") DEBUG = false
        } while (input.toLowerCase() != "c" && input.toLowerCase() != "q")
    }

    // count pointer positionss
}