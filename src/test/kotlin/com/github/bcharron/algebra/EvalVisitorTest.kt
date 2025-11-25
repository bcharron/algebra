package com.github.bcharron.algebra

import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.currentStackTrace

class EvalVisitorTest {
    @Test
    fun basicVisitorTest() {
        val node = Parser().parse("2x + 3 * 4")

        val b = Bindings(mapOf("x" to 1.0))
        val v = EvalVisitor(b)

        val result = node.accept(v)

        assertEquals(14.0, result, absoluteTolerance = 0.001)
    }
}
