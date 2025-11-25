package com.github.bcharron.algebra

interface Visitor<T> {
    fun visit(n: ExprNode): T
}
