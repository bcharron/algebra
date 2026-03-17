package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.ExprNode
import com.github.bcharron.algebra.NaryNode

class Addition(children: List<ExprNode>): NaryNode(children) {
    constructor(left: ExprNode, right: ExprNode): this(listOf(left, right))

    override fun toString(): String {
        val s = children.joinToString(" + ")
        return "($s)"
    }

    // this: [x, 1]
    // other: x
    // result: x.multiply(x) + x.multiply(1)
    override fun multiply(other: ExprNode) = Addition(children.map { it.multiply(other) })
}
