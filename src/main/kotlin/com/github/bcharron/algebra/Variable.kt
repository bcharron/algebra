package com.github.bcharron.algebra

import com.github.bcharron.algebra.extensions.plus
import com.github.bcharron.algebra.operations.Addition
import com.github.bcharron.algebra.operations.Exponent
import com.github.bcharron.algebra.operations.Multiplication

class Variable(val name: String) : ValueNode() {
    override fun toString() = name
    override fun equals(other: Any?): Boolean {
        return other != null && other is Variable && other.name == this.name
    }

    override fun multiply(other: ExprNode): ExprNode {
        val result = when (other) {
            is Variable -> {
                if (this.equals(other))
                // FIXME: Use copy here?
                    Exponent(this, Constant(2L))
                else
                    Multiplication(this, other)
            }

            is Exponent -> {
                if (this.equals(other.left) && other.right is Constant)
                    Exponent(this, Constant((other.right as Constant).n + 1L))
                else
                    Multiplication(this, other)
            }

            is Constant -> when (other.n) {
                1L -> this
                else -> Multiplication(this, other)
            }

            is ValueNode -> Multiplication(this, other)
            is Addition -> Addition(other.children.map { this.multiply(it) }) // x * (x + 1) -> (x*x) + (x*1) 
            else -> throw IllegalArgumentException("I can't multiply a ${other.javaClass} node")
        }

        return result
    }
}

