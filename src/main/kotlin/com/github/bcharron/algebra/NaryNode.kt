package com.github.bcharron.algebra

abstract class NaryNode(children: List<ExprNode>): ExprNode(children) {
    init {
        require(children.count() >= 2)
    }
}