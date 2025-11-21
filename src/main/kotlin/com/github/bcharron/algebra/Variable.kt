package com.github.bcharron.algebra

class Variable(val name: String): ExprNode() {
    override fun toString() = name
}
