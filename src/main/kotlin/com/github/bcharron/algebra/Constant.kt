package com.github.bcharron.algebra

import com.github.bcharron.algebra.operations.Multiplication
import com.github.bcharron.algebra.extensions.times
import com.github.bcharron.algebra.operations.Addition
import com.github.bcharron.algebra.operations.Substraction

class Constant(val n: Number): ValueNode() {
    override fun toString() = n.toString()

    override fun multiply(other: ExprNode): ExprNode {
        val result = when (other) {
            is Constant -> Constant(n * other.n)
            is Variable -> Multiplication(this, other)
            is Multiplication -> Multiplication(other.children + this)  // 4 * (x * 5) = 4 * x * 5
            is Addition -> Addition(other.children.map { Multiplication(this, it) })
            is Substraction -> Substraction(other.left.multiply(this), other.right.multiply(this))
            else -> throw IllegalArgumentException("Don't know how to deal with ${other.javaClass}")
        }

        // Do not simplify() here, it will recurse forever.
        return result
    }

    override fun simplify(): ExprNode = this
}
