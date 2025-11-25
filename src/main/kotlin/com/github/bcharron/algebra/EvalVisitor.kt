package com.github.bcharron.algebra

import kotlin.math.pow

class EvalVisitor(val bindings: Bindings): Visitor<Double> {
    var value = 0.0

    override fun visit(n: ExprNode): Double {
        // println("Visit ExprNode ${n}")

        val result = when (n) {
            is Addition -> n.left.accept(this) + n.right.accept(this)
            is Substraction -> n.left.accept(this) - n.right.accept(this)
            is Division -> n.left.accept(this) / n.right.accept(this)
            is Multiplication -> n.left.accept(this) * n.right.accept(this)
            is Exponent -> n.left.accept(this).pow(n.right.accept(this))
            is Constant -> n.n.toDouble()
            is Variable -> bindings.get(n.name)
            is Minus -> -n.value.accept(this)
            is Equals -> throw Exception("this should return true or false..")
            else -> throw Exception("Fuck you bro: ${n.javaClass}")
        }

        return result
    }
}
