package com.github.bcharron.algebra

import java.util.StringTokenizer

enum class Token {
    NUMBER,
    LEFT_PAREN,
    RIGHT_PAREN,
    PLUS,
    MINUS,
    SLASH,
}

fun main(args: Array<String>) {
    val parser = Parser()
    // val tree2 = parse(args.firstOrNull() ?: "0")
    val tree = parser.parse("2x+22 *9  - (4+2) = 15^2 + ln(4)")

    println("Tree: $tree")
}
