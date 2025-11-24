package com.github.bcharron.algebra

class Exponent(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "(${left})^(${right})"
    }
}
