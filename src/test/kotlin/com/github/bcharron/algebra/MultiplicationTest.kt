package com.github.bcharron.algebra

import com.github.bcharron.algebra.operations.Addition
import com.github.bcharron.algebra.operations.Exponent
import com.github.bcharron.algebra.operations.Multiplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MultiplicationTest {
    @Test
    fun `const times const`() {
        val a = Constant(2L)
        val b = Constant(3L)

        val result = a.multiply(b)

        assertIs<Constant>(result)
        assertEquals(result.n, 6L)
    }

    @Test
    fun `const times variable`() {
        val a = Constant(2L)
        val b = Variable("x")

        val result = a.multiply(b)

        val mul = assertIs<Multiplication>(result)
        val c1 = assertIs<Constant>(mul.children[0])
        val c2 = assertIs<Variable>(mul.children[1])

        assertEquals(2L, c1.n)
        assertEquals("x", c2.name)
    }

    @Test
    fun `variable times variable`() {
        val a = Variable("x")
        val b = Variable("x")

        val result = a.multiply(b)

        Exponent(
            Variable("x"),
            Constant(2L)
        ).assertTree(result)
    }

    @Test
    fun `variable times variable^2`() {
        val a = Variable("x")
        val b = Exponent(Variable("x"), Constant(2L))

        val result = a.multiply(b)

        Exponent(
            Variable("x"),
            Constant(3L)
        ).assertTree(result)
    }

    @Test
    fun `variable times addition`() {
        val a = Variable("x")
        val b = Addition(Variable("x"), Constant(2L))

        // x * (x + 2) = x^2 + 2x
        val result = a.multiply(b)

        Addition(
            Exponent(
                Variable("x"),
                Constant(2L)
            ),
            Multiplication(
                Variable("x"),
                Constant(2L)
            )
        ).assertTree(result)

        println(result.toString())
    }

    @Test
    fun `variable times identity`() {
        val a = Variable("x")
        val b = Constant(1L)

        val result = a.multiply(b)

        Variable("x").assertTree(result)

        println(result.toString())
    }

}