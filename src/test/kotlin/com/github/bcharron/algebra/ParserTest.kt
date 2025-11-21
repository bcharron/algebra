package com.github.bcharron.algebra

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs

class ParserTest {
    @Test
    fun parseStringTest() {
        val p = Parser()
        val (s, n) = p.readString("abc22")

        assertEquals(s, "abc")
        assertEquals(n, 3)
    }

    @Test
    fun parseNumberTest() {
        val p = Parser()
        val (s, n) = p.readNumber("22.6abc")

        assertEquals(s, "22.6")
        assertEquals(n, 4)
    }

    @Test
    fun tokenizeTest() {
        val p = Parser()
        val expr = p.tokenize("(1+22x)*7=60")
        val expected = arrayOf("(", "1", "+", "22", "x", ")", "*", "7", "=", "60")

        assertTrue(expected.contentEquals(expr.toTypedArray()))
    }

    @Test
    fun buildTreeTest1() {
        val p = Parser()
        val expr = listOf("2", "+", "4")

        val tree = p.buildTree(expr)

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Constant>(tree.right)
        assertEquals(tree.left.n, 2)
        assertEquals(tree.right.n, 4)
    }

    @Test
    fun buildTreeTest2() {
        val p = Parser()
        val expr = listOf("2", "+", "4", "*", "2")

        val tree = p.buildTree(expr)

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.left)
        assertEquals(tree.left.n, 2)

        val mul = tree.right
        assertIs<Multiplication>(mul)
        assertIs<Constant>(tree.left)
        assertIs<Constant>(tree.right)
        assertEquals(tree.left.n, 4)
        assertEquals(tree.right.n, 2)
    }
}
