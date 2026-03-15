package com.github.bcharron.algebra

interface Visitor<T> {
    fun visit(node: ExprNode): T
}
