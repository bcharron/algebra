package com.github.bcharron.algebra

class Equals(l: ExprNode, r: ExprNode): BinaryNode(l, r) {
    override fun toString(): String {
        return "${left} = ${right}"
    }

    // override fun eval(): Number {
    //     if (left == right) {
    //         return 1
    //     } else {
    //         return 0
    //     }
    // }
}
