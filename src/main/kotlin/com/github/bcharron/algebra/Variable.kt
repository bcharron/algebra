package com.github.bcharron.algebra

class Variable(val name: String): ValueNode() {
    override fun toString() = name
}
