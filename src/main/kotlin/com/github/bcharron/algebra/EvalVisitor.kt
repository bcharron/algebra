package com.github.bcharron.algebra

import com.github.bcharron.algebra.operations.Multiplication
import kotlin.math.pow

class EvalVisitor(val bindings: Bindings): Visitor<Double> {
    var value = 0.0

    override fun visit(node: ExprNode): Double {
        // println("Visit ExprNode ${n}")

        val result = when (node) {
            // is Addition -> node.left.accept(this) + node.right.accept(this)
            // is Substraction -> node.left.accept(this) - node.right.accept(this)
            // is Division -> node.left.accept(this) / node.right.accept(this)
            // is Multiplication -> node.left.accept(this) * node.right.accept(this)
            // is Exponent -> node.left.accept(this).pow(node.right.accept(this))
            // is Constant -> node.n.toDouble()
            // is Variable -> bindings.get(node.name)
            // is Minus -> -node.value.accept(this)
            // is Equals -> throw Exception("this should return true or false..")
            else -> throw Exception("Fuck you bro: ${node.javaClass}")
        }

        return result
    }
}
