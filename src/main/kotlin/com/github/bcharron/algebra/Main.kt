package com.github.bcharron.algebra

fun parseArgs(args: Array<String>): Pair<String?, Bindings?> {
    if (args.size == 0) {
        return null to null
    }

    val expr = args.first()
    if ("|" in expr) {
        val (lhs, rhs) = args.first().split("|", limit = 2)

        val params =
            rhs
                .split(",")
                .map {
                    it.split("=", limit = 2).let { it[0] to it[1].toDouble() }
                }.toMap()

        println("params: $params")
        val bindings = Bindings(params)

        return lhs to bindings
    } else {
        return expr to null
    }
}

fun main(args: Array<String>) {
    val parser = Parser()

    val (expr, bindings) = parseArgs(args)
    if (expr != null) {
        val tree = parser.parse(expr)
        println(tree)

        if (bindings != null) {
            val v = EvalVisitor(bindings)
            val result = tree.accept(v)
            println(result)
        }
    } else {
        // val tree2 = parse(args.firstOrNull() ?: "0")
        val tree = parser.parse("2x+22 *9  - (4+2) = 15^2 + ln(4)")
        println("Tree: $tree")

        val b = Bindings(mapOf("x" to 50.0))
        val v = EvalVisitor(b)

        val tree2 = parser.parse("2*3+4+x")
        println(tree2)
        val result = tree2.accept(v)
        println("result: $result")
    }
}
