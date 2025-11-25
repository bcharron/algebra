package com.github.bcharron.algebra

class Exponent(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        val l = if (left is ValueNode) left.toString() else "($left)"
        val r = if (right is ValueNode) right.toString() else "($right)"
        return "$left^$right"
    }
}
