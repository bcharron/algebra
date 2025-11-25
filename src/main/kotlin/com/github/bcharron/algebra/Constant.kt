package com.github.bcharron.algebra

class Constant(val n: Number): ValueNode() {
    override fun toString() = n.toString()
}
