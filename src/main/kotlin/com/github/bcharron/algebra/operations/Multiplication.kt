package com.github.bcharron.algebra

class Multiplication(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "(${left} * ${right})"
    }
}
