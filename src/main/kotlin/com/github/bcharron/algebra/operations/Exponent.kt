package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.BinaryNode
import com.github.bcharron.algebra.ExprNode
import com.github.bcharron.algebra.ValueNode

class Exponent(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        val l = if (left is ValueNode) left.toString() else "($left)"
        val r = if (right is ValueNode) right.toString() else "($right)"
        return "$l^$r"
    }
}
