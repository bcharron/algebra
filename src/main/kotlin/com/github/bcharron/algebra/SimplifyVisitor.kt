package com.github.bcharron.algebra

import kotlin.math.pow

fun isSameVariable(left: ExprNode, right: ExprNode): Boolean = left.equals(right)

fun isConstTimesVariable(left: ExprNode) {
}

class SimplifyVisitor(): Visitor<ExprNode> {
    var value = 0.0

    override fun visit(node: ExprNode): ExprNode = node
/*
    override fun visit(node: ExprNode): ExprNode {
        // println("Visit ExprNode ${node}")

        // 2x + 4x
        when (node) {
            is Addition -> {
                val type = node.left.accept(this)
                if (isSameVariable(node.left, node.right)) {
                }
            }
            is Substraction -> node.left.accept(this) - node.right.accept(this)
            is Division -> node.left.accept(this) / node.right.accept(this)
            is Multiplication -> node.left.accept(this) * node.right.accept(this)
            is Exponent -> node.left.accept(this).pow(node.right.accept(this))
            is Constant -> node.n.toDouble()
            // is Variable -> bindings.get(node.name)
            is Minus -> -node.value.accept(this)
            is Equals -> throw Exception("this should return true or false..")
            // else -> throw Exception("Fuck you bro: ${node.javaClass}")
        }

        return result
    }
    */
}
