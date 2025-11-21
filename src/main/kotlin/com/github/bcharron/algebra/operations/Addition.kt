package com.github.bcharron.algebra

class Addition(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "(${left} + ${right})"
    }
}
