package com.github.bcharron.algebra

class Minus(value: ExprNode): UnaryNode(value) {
    override fun toString(): String {
        return "-(${value})"
    }
}
