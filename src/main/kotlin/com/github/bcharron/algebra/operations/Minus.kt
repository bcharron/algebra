package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.ExprNode
import com.github.bcharron.algebra.UnaryNode

class Minus(value: ExprNode): UnaryNode(value) {
    override fun toString(): String {
        return "-(${value})"
    }
}
