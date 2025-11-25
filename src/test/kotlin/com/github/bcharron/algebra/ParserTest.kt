package com.github.bcharron.algebra

import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.currentStackTrace

class ParserTest {
    @Test
    fun parseStringTest() {
        val (s, n) = Parser().readString("abc22")

        assertEquals(s, "abc")
        assertEquals(n, 3)
    }

    @Test
    fun parseNotAString() {
        val (s, n) = Parser().readString("342-=2")

        assertTrue(s.isEmpty())
        assertEquals(n, 0)
    }

    @Test
    fun parseNumberTest() {
        val (s, n) = Parser().readNumber("22.6abc")

        assertEquals(s, "22.6")
        assertEquals(n, 4)
    }

    @Test
    fun tokenizeTest() {
        val expr = Parser().tokenize("(1+22x)*7=60")
        val expected = arrayOf("(", "1", "+", "22", "x", ")", "*", "7", "=", "60")

        assertTrue(expected.contentEquals(expr.toTypedArray()))
    }

    @Test
    fun findLowestPriorityTest() {
        val elements = listOf("1", "+", "2")

        val lowest = Parser().findLowestPriority(elements)
        assertEquals(1, lowest)
    }

    @Test
    fun findLowestPriorityParensTest() {
        val p = Parser()
        val elements = listOf("1", "*", "(", "2", "+", "3", ")")

        val lowest = p.findLowestPriority(elements)
        assertEquals(1, lowest)
        assertEquals("*", elements.get(lowest))
    }

    @Test
    fun trimParensNormal() {
        val p = Parser()
        val elements = listOf("(", "2", "+", "3", ")")

        val trimmed = p.trimParens(elements)

        assertEquals(3, trimmed.size)
        assertContentEquals(listOf("2", "+", "3"), trimmed)
    }

    @Test
    fun trimParensDouble() {
        val p = Parser()
        val elements = listOf("(", "(", "2", "+", "3", ")", ")")

        val trimmed = p.trimParens(elements)

        assertEquals(3, trimmed.size)
        assertContentEquals(listOf("2", "+", "3"), trimmed)
    }

    @Test
    fun trimParensNothingToDo() {
        val p = Parser()
        val elements = listOf("(", "2", ")", "+", "(", "2", ")")

        val trimmed = p.trimParens(elements)

        assertEquals(7, trimmed.size)
        assertContentEquals(elements, trimmed)
    }

    @Test
    fun testSplitLowestSimple() {
        val expr = listOf("2", "+", "4")

        val tree = Parser().splitLowest(expr)

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Constant>(tree.right)
        assertEquals(tree.left.n, 2L)
        assertEquals(tree.right.n, 4L)
    }

    @Test
    fun testSplitLowestTwoOperations() {
        val expr = listOf("2", "+", "4", "*", "2")

        val tree = Parser().splitLowest(expr)

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.left)
        assertEquals(tree.left.n, 2L)

        val mul = tree.right
        assertIs<Multiplication>(mul)
        assertIs<Constant>(mul.left)
        assertIs<Constant>(mul.right)
        assertEquals(mul.left.n, 4L)
        assertEquals(mul.right.n, 2L)
    }

    @Test
    fun testSplitLowestPriorityOfOperations() {
        val expr = listOf("2", "*", "4", "+", "2")

        val tree = Parser().splitLowest(expr)

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.right)
        assertEquals(tree.right.n, 2L)

        val mul = tree.left
        assertIs<Multiplication>(mul)
        assertIs<Constant>(mul.left)
        assertIs<Constant>(mul.right)
        assertEquals(mul.left.n, 2L)
        assertEquals(mul.right.n, 4L)
    }

    @Test
    fun testSplitLowestWithParens() {
        val expr = listOf("2", "*", "(", "4", "+", "2", ")")

        val tree = Parser().splitLowest(expr)

        assertIs<Multiplication>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Addition>(tree.right)
        assertEquals(tree.left.n, 2L)

        val add = tree.right
        assertIs<Addition>(add)
        assertIs<Constant>(add.left)
        assertIs<Constant>(add.right)
        assertEquals(add.left.n, 4L)
        assertEquals(add.right.n, 2L)
    }

    @Test
    fun expandTestBasic() {
        val expr = listOf("2", "+", "4")
        val filtered = Parser().expand(expr)

        assertContentEquals(expr, filtered)
    }

    @Test
    fun testSplitConstantTimesVariable() {
        val expr = listOf("2", "x")

        val expanded = Parser().expand(expr)

        val expected = listOf("2", "*", "x")
        assertContentEquals(expected, expanded)
    }

    @Test
    fun testParseEquality() {
        val expr = "x + 2 = 4"

        val tree = Parser().parse(expr)

        assertIs<Equals>(tree)
        assertIs<Addition>(tree.left)
        assertIs<Constant>(tree.right)
        assertEquals(4L, tree.right.n)

        val add = tree.left
        assertIs<Addition>(add)
        assertIs<Variable>(add.left)
        assertIs<Constant>(add.right)
        assertEquals(2L, add.right.n)
        assertEquals("x", add.left.name)
    }

    @Test
    fun testParseExpanded() {
        val tree = Parser().parse("2x^2")

        assertIs<Multiplication>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Exponent>(tree.right)
        assertEquals(2L, tree.left.n)

        val exp = tree.right
        assertIs<Variable>(exp.left)
        assertIs<Constant>(exp.right)
        assertEquals("x", exp.left.name)
        assertEquals(2L, exp.right.n)
    }

    @Test
    fun testParseMinus() {
        val tree = Parser().parse("-2")

        assertIs<Constant>(tree)
        assertEquals(-2L, tree.n)
    }

    @Test
    fun testParseMinusParens() {
        val tree = Parser().parse("-(2)")

        assertIs<Constant>(tree)
        assertEquals(-2L, tree.n)
    }

    @Test
    fun testParseMinusVariable() {
        val tree = Parser().parse("-(x)")

        assertIs<Minus>(tree)
        assertIs<Variable>(tree.value)
        assertEquals("x", tree.value.name)
    }

    @Test
    fun testParseMinusExpr() {
        val tree = Parser().parse("-(2+4)")

        assertIs<Minus>(tree)
        assertIs<Addition>(tree.value)

        val add = tree.value
        assertIs<Constant>(add.left)
        assertIs<Constant>(add.right)
        assertEquals(2L, add.left.n)
        assertEquals(4L, add.right.n)
    }

    @Test
    fun testParseParens() {
        val tree = Parser().parse("(2+3)")

        assertIs<Addition>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Constant>(tree.right)
        assertEquals(2L, tree.left.n)
        assertEquals(3L, tree.right.n)
    }

    @Test
    fun testLeftAssociative() {
        val tree = Parser().parse("a - b - c")

        assertIs<Substraction>(tree)
        assertIs<Substraction>(tree.left)
        assertIs<Variable>(tree.right)
        assertEquals("c", tree.right.name)

        val sub = tree.left
        assertIs<Variable>(sub.left)
        assertIs<Variable>(sub.right)
        assertEquals("a", sub.left.name)
        assertEquals("b", sub.right.name)
    }

    @Test
    fun testRightAssociative() {
        val tree = Parser().parse("2^3^4")

        assertIs<Exponent>(tree)
        assertIs<Constant>(tree.left)
        assertIs<Exponent>(tree.right)
        assertEquals(2L, tree.left.n)

        val exp = tree.right

        assertIs<Exponent>(exp)
        assertIs<Constant>(exp.left)
        assertIs<Constant>(exp.right)
        assertEquals(3L, exp.left.n)
        assertEquals(4L, exp.right.n)
    }

    @Test
    fun testUltimateParse() {
        val tree = Parser().parse("2x^2+4x+9=0")

        assertIs<Equals>(tree)
        assertIs<Addition>(tree.left)
        assertIs<Constant>(tree.right)

        val add = tree.left

        assertIs<Addition>(add.left)
        assertIs<Constant>(add.right)
        assertEquals(9L, add.right.n)
    }
}
