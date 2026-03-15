package com.github.bcharron.algebra

abstract class ExprNode(val children: List<ExprNode>) {
    fun <R> accept(v: Visitor<R>): R {
        return v.visit(this)
    }

    open fun simplify(): ExprNode {
        return this
    }

    open fun hasChildren(): Boolean = children.count() > 0

    open fun multiply(other: ExprNode): ExprNode {
        throw IllegalArgumentException("multiply() not defined for ${this.javaClass}")
    }
}
