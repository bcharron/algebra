package com.github.bcharron.algebra

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

        assertTrue(expected.contentEquals(expr))
    }
}
