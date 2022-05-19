package com.github.keelar.exprk

import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import kotlin.test.assertEquals

class TestEvaluateExpression {
    @Test
    fun `evaluate expression with no variables`() {
        val value = Expressions().evaluateExpression("1 + 2", mapOf())
        assertEquals(BigDecimal(3), value)
    }

    @Test
    fun `evaluate expression with variables`() {
        val value = Expressions().evaluateExpression("a + b",
            mapOf("a" to "1.2",
                "b" to "2"))
        assertEquals(BigDecimal(3.2, MathContext.DECIMAL64).stripTrailingZeros(), value)
    }

    @Test(expected = ExpressionException::class)
    fun `evaluate expression with undefined variables`() {
        Expressions().evaluateExpression("a + b",
            mapOf("a" to "1"))
    }

}