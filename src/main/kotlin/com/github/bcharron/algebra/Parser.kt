package com.github.bcharron.algebra

class Parser {
    // Read a number from the string
    fun readNumber(s: String): Pair<String, Int> {
        var sb = StringBuilder()

        var it = s.iterator()

        var pos = 0
        while (it.hasNext()) {
            var c = it.next()

            if (c.isDigit() || c == '.') {
                sb.append(c)
                pos++
            } else {
                break
            }
        }

        return sb.toString() to pos
    }

    fun readString(s: String): Pair<String, Int> {
        var sb = StringBuilder()

        var it = s.iterator()

        var pos = 0
        while (it.hasNext()) {
            var c = it.next()

            if (c.isLetter()) {
                sb.append(c)
                pos++
            } else {
                break
            }
        }

        return sb.toString() to pos
    }

    // Split expression into a list of tokens
    fun tokenize(expr: String): Array<String> {
        var list = mutableListOf<String>()
        val operators = setOf('(', ')', '+', '-', '/', '*', '^', '=')

        var x = 0
        while (x < expr.length) {
            val c = expr[x]

            when {
                c.isDigit() -> {
                    val (value, pos) = readNumber(expr.substring(x))
                    x += pos
                    list.add(value)
                }

                c.isLetter() -> {
                    val (value, pos) = readString(expr.substring(x))
                    x += pos
                    list.add(value)
                }

                c in operators -> {
                    list.add(c.toString())
                    x++
                }

                c.isWhitespace() -> x++

                else -> throw IllegalArgumentException("Invalid character: $c")
            }
        }

        return list.toTypedArray()
    }

    fun buildTree(elements: Array<String>) {
    }

    fun parse(expr: String): ExprNode {
        val elements = tokenize(expr)

        for (entry in elements) {
            println("entry: [$entry]")
        }

        return Constant(1)
    }
}
