package com.github.bcharron.algebra

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

fun ExprNode.assertConst(n: Number) {
    val c = assertIs<Constant>(this)
    assertEquals(n, c.n)
}

fun ExprNode.assertVar(name: String) {
    val c = assertIs<Variable>(this)
    assertEquals(name, c.name)
}

fun ExprNode.assertTree(expected: ExprNode) {
    assertThat(this).hasClass(expected::class.java)
    assertThat(this.children).hasSameSizeAs(expected.children)

    when (expected) {
        is Constant -> assertEquals(expected.n, (this as Constant).n)
        is Variable -> assertEquals(expected.name, (this as Variable).name)
        is BinaryNode -> {
            (this as BinaryNode).left.assertTree(expected.left)
            (this as BinaryNode).right.assertTree(expected.right)
        }
        is NaryNode -> {
            for ((i, value) in expected.children.withIndex())
                this.children[i].assertTree(expected.children[i])
        }
    }
}
