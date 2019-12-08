package day8

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Must provide input data")
        System.exit(0)
    }

    val input = args[0].map { Character.getNumericValue(it) }

    val imgSize = 25 * 6

    val layers = input.chunked(imgSize)

    val layer = layers.minBy { it.count { it == 0 } }!!

    val part1Answer = layer.count { it == 1 } * layer.count {it == 2}

    println("Part 1: $part1Answer")

    val img = decode(layers)
    println(img.replace('0', ' '))
}


fun decode(layers: List<List<Int>>): String {
    val final = layers.last().toMutableList()
    layers.dropLast(1).asReversed().forEach { layer ->
        layer.forEachIndexed { index, pixel ->
            if (pixel == 0 || pixel == 1) {
                final[index] = pixel
            }
        }
    }
    return final.chunked(25).joinToString("\n") { it.joinToString("") }
}