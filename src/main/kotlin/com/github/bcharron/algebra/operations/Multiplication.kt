package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.Bindings
import com.github.bcharron.algebra.Constant
import com.github.bcharron.algebra.ExprNode
import com.github.bcharron.algebra.NaryNode
import com.github.bcharron.algebra.ValueNode

class Multiplication(nodes: List<ExprNode>): NaryNode(nodes) {
    constructor(left: ExprNode, right: ExprNode): this(listOf(left, right))

    override fun toString(): String {
        return "(" + children.joinToString("*") + ")"
    }

    override fun simplify(): ExprNode {
        if (children.none { n -> n.hasChildren() }) {
            // Nothing to do, all children are already values
            return this
        }

        var newNodes = mutableListOf<ExprNode>()

        // x*y*(x+1) -> x^2+x+yx+y
        // x*y*(x*x) -> x^2*x^2
        for (x in 0..<children.count()) {
            val left = children[x]

            for (y in 0..<children.count()) {
                if (x == y)
                    continue

                val right = children[y]

                if (left is ValueNode && right is ValueNode) {
                    newNodes.add(left)
                    newNodes.add(right)
                } else {
                    
                }
            }
        }

        return Multiplication(newNodes.toList())
    }

    // One Multiplication (x*2) mutiplies another (x*4) -> each children multiplies the other element
    override fun multiply(other: ExprNode): ExprNode = Multiplication(children.map { it.multiply(other) })

    // fun eval(bindings: Bindings): Number {
    //     return children[0].evalMultiply(bindings, children[1])
    // }

    /*
    fun isConstantTimesVariable(): Boolean {
        if (left is Constant && right is Variable) {
            return true
        }

        return false
    }
    */

    /*
    fun isCompatible(): Boolean {
    }
    */
}
