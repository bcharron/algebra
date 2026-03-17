package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.Bindings
import com.github.bcharron.algebra.Constant
import com.github.bcharron.algebra.ExprNode
import com.github.bcharron.algebra.NaryNode
import com.github.bcharron.algebra.ValueNode
import com.github.bcharron.algebra.Variable
import com.github.bcharron.algebra.extensions.plus
import com.github.bcharron.algebra.extensions.times

class Multiplication(nodes: List<ExprNode>): NaryNode(nodes) {
    constructor(left: ExprNode, right: ExprNode): this(listOf(left, right))

    override fun toString(): String {
        return "(" + children.joinToString("*") + ")"
    }

    override fun simplify(): ExprNode {
        /*
        // FIXME: Actually, constants and variables of the same type can be multiplied together
        if (children.all { n -> n is ValueNode }) {
            // Nothing to do, no children or all children are already values
            return this
        }
        */

        var newNodes = mutableListOf<ExprNode>()

        // x*y*(x+1) -> x^2+x+yx+y
        // x*y*(x*x) -> x^2*x^2
        for (x in children.indices) {
            for (y in x + 1 until children.size) {
                var left = children[x]
                val right = children[y]

                val result = left.multiply(right)
                if (result != null) {
                    newNodes.add(result)
                }

                // if (left is ValueNode && right is ValueNode) {
                //     if (left is Constant && right is Constant) {
                //         newNodes.add(Constant(left.n * right.n))
                //     } else if (left is Variable && right is Variable) {
                //         if (left.equals(right)) {
                //             newNodes.add(Exponent(left, Constant(2L)))
                //         } else {
                //             newNodes.add(left)
                //             newNodes.add(right)
                //         }
                //     } else {
                //         newNodes.add(left)
                //         newNodes.add(right)
                //     }
                // } else {
                    
                // }
            }
        }

        return Multiplication(newNodes.toList())
    }

    // One Multiplication (x*2) mutiplies something else (x+9)
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
