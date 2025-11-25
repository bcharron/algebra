package com.github.bcharron.algebra

abstract class ExprNode {
    fun <R> accept(v: Visitor<R>): R {
        return v.visit(this)
    }
}
