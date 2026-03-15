package com.github.bcharron.algebra

import com.github.bcharron.algebra.operations.Multiplication

operator fun Number.times(other: Number): Number {
    if (this is Long && other is Long) {
        return this * other
    }

    return this.toDouble() * other.toDouble()
}

operator fun Number.plus(other: Number): Number {
    if (this is Long && other is Long) {
        return this + other
    }

    return this.toDouble() + other.toDouble()
}

class Constant(val n: Number): ValueNode() {
    override fun toString() = n.toString()

    override fun multiply(other: ExprNode): ExprNode {
        val result = when (other) {
            is Constant -> Constant(n * other.n)
            is Variable -> Multiplication(listOf(this, other))
            else -> throw IllegalArgumentException("Don't know how to deal with ${other.javaClass}")
        }

        return result
    }
}
