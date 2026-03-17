package com.github.bcharron.algebra.operations

import com.github.bcharron.algebra.BinaryNode
import com.github.bcharron.algebra.ExprNode

class Equals(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "$left = $right"
    }

    // override fun eval(): Number {
    //     if (left == right) {
    //         return 1
    //     } else {
    //         return 0
    //     }
    // }
}
