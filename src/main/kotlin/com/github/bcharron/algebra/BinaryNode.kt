package com.github.bcharron.algebra

abstract class BinaryNode(left: ExprNode, right: ExprNode) : ExprNode(listOf(left, right)) {
    init {
        require(children.size == 2)
    }

    val left get() = children[0]
    val right get() = children[1]
}
