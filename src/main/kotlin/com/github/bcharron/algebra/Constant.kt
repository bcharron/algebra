package com.github.bcharron.algebra

class Constant(val n: Number): ExprNode() {
    override fun toString() = n.toString()
}
