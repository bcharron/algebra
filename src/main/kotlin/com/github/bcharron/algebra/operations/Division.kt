package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.BinaryNode
import com.github.bcharron.algebra.ExprNode

class Division(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "(${left} / ${right})"
    }
}
