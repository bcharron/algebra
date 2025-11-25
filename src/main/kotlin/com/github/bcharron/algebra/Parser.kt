package com.github.bcharron.algebra

// Find highest priority -> if nothing found, done
// Resolve it, replace all matched tokens with ExprNode
import com.github.bcharron.algebra.UnaryNode
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.collections.elementAtOrElse

class Parser {
    private final enum class OperatorType {
        Value,
        Unary,
        Binary,
    }

    private data class Operator(
        val value: String,
        val prio: Int,
        val type: OperatorType,
        val cls: Class<out ExprNode>,
    )

    private final val operators =
        mapOf(
            "(" to Operator("(", 6, OperatorType.Binary, ExprNode::class.java),
            ")" to Operator(")", 6, OperatorType.Binary, ExprNode::class.java),
            "^" to Operator("^", 5, OperatorType.Binary, Exponent::class.java),
            "/" to Operator("/", 4, OperatorType.Binary, Division::class.java),
            "*" to Operator("*", 3, OperatorType.Binary, Multiplication::class.java),
            "-" to Operator("-", 2, OperatorType.Binary, Substraction::class.java),
            "+" to Operator("+", 1, OperatorType.Binary, Addition::class.java),
            "=" to Operator("-", 0, OperatorType.Binary, Equals::class.java),
        )

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

    /*
     * Split expression into a list of tokens
     */
    fun tokenize(expr: String): List<String> {
        var list = mutableListOf<String>()

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

                c.toString() in operators -> {
                    list.add(c.toString())
                    x++
                }

                c.isWhitespace() -> x++

                else -> throw IllegalArgumentException("Invalid character: $c")
            }
        }

        return list.toList()
    }

    /**
     * Remove leading and trailing parentheses
     * Safe to call even if there are no leading parentheses
     */
    fun trimParens(list: List<String>): List<String> {
        val out: List<String>

        if (list.first() == "(" && list.last() == ")") {
            var parens = 0

            for (c in list.subList(1, list.size - 1)) {
                when (c) {
                    "(" -> parens++
                    ")" -> parens--
                    else -> {}
                }

                // Found something like (1+2)+(3+4)
                if (parens < 0) {
                    break
                }
            }

            if (parens < 0) {
                out = list
            } else {
                // Trimmed something? Then keep going until we've removed all "redundant" parens
                out = trimParens(list.subList(1, list.lastIndex))
            }
        } else {
            // Doesn't match '(.*)', we're done.
            out = list
        }

        return out
    }

    /**
     * Returns the index of the operator with the lowest priority
     */
    fun findLowestPriority(elements: List<String>): Int {
        var lowestIndex = -1
        var lowestValue = 99

        var parens = 0

        // Go right to left to handle left-associative operations like Substraction
        for (idx in elements.indices.reversed()) {
            val entry = elements[idx]

            when (entry) {
                "(" -> parens--
                ")" -> parens++
                else -> {
                    val prio = getOperatorPrio(entry)

                    if (parens > 0) {
                        continue
                    }

                    // Special case for exponents which are right-associative
                    if ((prio < lowestValue) || (entry == "^" && prio <= lowestValue)) {
                        lowestIndex = idx
                        lowestValue = prio
                    }
                }
            }
        }

        return lowestIndex
    }

    private fun getOperatorType(s: String) = operators[s]?.type ?: OperatorType.Value

    private fun getOperatorPrio(s: String) = operators[s]?.prio ?: 98

    private fun createValueNode(s: String): ExprNode {
        if (s[0].isDigit()) {
            val i = if ('.' in s) s.toDouble() else s.toLong()
            return Constant(i)
        } else if (s[0].isLetter()) {
            return Variable(s)
        }

        throw IllegalArgumentException("$s is not a number or a constant")
    }

    private fun createUnaryOperator(
        op: String,
        node: ExprNode,
    ) = when (op) {
        "-" -> minusExpr(node)
        else -> throw Exception("Unknown unary operator: '$op'")
    }

    private fun createBinaryOperator(
        op: String,
        a: ExprNode,
        b: ExprNode,
    ): ExprNode {
        val expr =
            when (op) {
                "^" -> Exponent(a, b)
                "*" -> Multiplication(a, b)
                "/" -> Division(a, b)
                "+" -> Addition(a, b)
                "-" -> Substraction(a, b)
                "=" -> Equals(a, b)
                else -> throw IllegalArgumentException("$op is not a binary operator")
            }

        return expr
    }

    /*
     * Find the lowest prio operator in the list, split list in two (both
     * sides of the operator), then recursively resolve both sides.
     */
    fun splitLowest(list: List<String>): ExprNode {
        val elements = trimParens(list)
        val idx = findLowestPriority(elements)

        val entry = elements.get(idx)

        val opType =
            when {
                entry.equals("-") && idx == 0 -> OperatorType.Unary
                else -> getOperatorType(entry)
            }

        val expr =
            when (opType) {
                OperatorType.Value -> createValueNode(entry)

                OperatorType.Unary -> {
                    val right = trimParens(elements.subList(idx + 1, elements.size))
                    createUnaryOperator(entry, splitLowest(right))
                }

                OperatorType.Binary -> {
                    val left = trimParens(elements.subList(0, idx))
                    val right = trimParens(elements.subList(idx + 1, elements.size))

                    createBinaryOperator(entry, splitLowest(left), splitLowest(right))
                }
            }

        return expr
    }

    fun minusExpr(node: ExprNode) =
        when (node) {
            is Constant -> negateConstant(node.n)
            else -> Minus(node)
        }

    fun negateConstant(n: Number) =
        when (n) {
            is BigDecimal -> Constant(n.negate())
            is BigInteger -> Constant(n.negate())
            is Double -> Constant(-n)
            is Long -> Constant(-n)
            else -> throw Exception("invalid type, '${n.javaClass}'")
        }

    /*
     * Expand expressions like "2x" into "2 * x"
     */
    fun expand(elements: List<String>): List<String> {
        if (elements.size < 2) {
            return elements
        }

        val list1 = elements.subList(0, elements.size - 1)
        val list2 = elements.subList(1, elements.size)

        val middle =
            list1
                .zip(list2) { a, b ->
                    // Expressions like '2x' should be converted to '2*x'
                    if (getOperatorType(a) == OperatorType.Value && getOperatorType(b) == OperatorType.Value) {
                        listOf(a, "*")
                    } else {
                        listOf(a)
                    }
                }.flatten()

        return middle + list2.last()
    }

    fun parse(expr: String): ExprNode {
        val elements = tokenize(expr)
        val expanded = expand(elements)
        val headNode = splitLowest(expanded)

        return headNode
    }
}
