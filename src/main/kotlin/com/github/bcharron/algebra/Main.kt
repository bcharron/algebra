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
    val mul = Multiplication(Variable("x"), Constant(2))
    val addition = Addition(Constant(1), mul)
    val two = Constant(2)
    val equals = Equals(addition, two)
    val tree = equals

    println("Tree: $tree")

    val parser = Parser()
    // val tree2 = parse(args.firstOrNull() ?: "0")
    val tree2 = parser.parse("2x+22 *9  - (4+2) = 15^2 + ln(4)")

    println("Tree 2: $tree2")
}
