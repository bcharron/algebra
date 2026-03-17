package com.github.bcharron.algebra

import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.currentStackTrace
import assertk.assertThat
import assertk.assertions.*
import com.github.bcharron.algebra.operations.Addition
import com.github.bcharron.algebra.operations.Equals
import com.github.bcharron.algebra.operations.Exponent
import com.github.bcharron.algebra.operations.Minus
import com.github.bcharron.algebra.operations.Multiplication
import com.github.bcharron.algebra.operations.Substraction

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

        Addition(
            Constant(2L),
            Constant(4L)
        ).assertTree(tree)
    }

    @Test
    fun testSplitLowestTwoOperations() {
        val expr = listOf("2", "+", "4", "*", "2")

        val tree = Parser().splitLowest(expr)

        Addition(
            Constant(2L),
            Multiplication(
                Constant(4L),
                Constant(2L)
            )
        ).assertTree(tree)
    }

    @Test
    fun testSplitLowestPriorityOfOperations() {
        val expr = listOf("2", "*", "4", "+", "2")

        val tree = Parser().splitLowest(expr)

        Addition(
            listOf(
                Multiplication(
                    listOf(
                        Constant(2L),
                        Constant(4L)
                    )
                ),
                Constant(2L)
            )
        ).assertTree(tree)
    }

    @Test
    fun testSplitLowestWithParens() {
        val expr = listOf("2", "*", "(", "4", "+", "2", ")")

        val tree = Parser().splitLowest(expr)

        Multiplication(
            Constant(2L),
            Addition(
                Constant(4L),
                Constant(2L)
            )
        ).assertTree(tree)
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

        val expected = Equals(
            Addition(
                listOf(
                    Variable("x"),
                    Constant(2)
                ),
            ),
            Constant(4)
        )
    }

    @Test
    fun testParseExpanded() {
        val tree = Parser().parse("2x^2")

        Multiplication(
            Constant(2L),
            Exponent(
                Variable("x"),
                Constant(2L)
            )
        ).assertTree(tree)
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

        Minus(
            Addition(
                Constant(2L),
                Constant(4L)
            )
        ).assertTree(tree)
    }

    @Test
    fun testParseParens() {
        val tree = Parser().parse("(2+3)")

        Addition(
            Constant(2L), Constant(3L)
        ).assertTree(tree)
    }

    @Test
    fun testLeftAssociative() {
        val tree = Parser().parse("a - b - c")

        Substraction(
            Substraction(
                Variable("a"),
                Variable("b")
            ),
            Variable("c")
        ).assertTree(tree)
    }

    @Test
    fun testRightAssociative() {
        val tree = Parser().parse("2^3^4")

        Exponent(
            Constant(2L),
            Exponent(
                Constant(3L),
                Constant(4L)
            )
        ).assertTree(tree)
    }

    @Test
    fun testUltimateParse() {
        val tree = Parser().parse("2x^2+4x+9=0")

        Equals(
            Addition(
                Addition(              
                    Multiplication(
                        Constant(2L),
                        Exponent(
                            Variable("x"),
                            Constant(2L)
                        )
                    ),
                    Multiplication(
                        Constant(4L),
                        Variable("x")
                    )),
                    Constant(9L)
            ),
            Constant(0L)
        ).assertTree(tree)
    }
}
