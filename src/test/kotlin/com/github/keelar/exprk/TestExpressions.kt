package com.github.keelar.exprk

import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class TestExpressions {

    @Test
    fun `test that scientific notation BigDecimals are parsed and equivalent to the plain representation`() {
        val expr = Expressions()
        val scival = BigDecimal("1E+7")
        expr.define("SCIVAL", scival)
        assertEquals(scival.toPlainString(), expr.eval("SCIVAL").toPlainString())
    }
}