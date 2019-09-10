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

    @Test
    fun `test Scanner will scan scientific form correctly`(){
        val expr = Expressions()
        assertEquals(BigDecimal("1e+7").toPlainString(), expr.eval("1E+7").toPlainString())
        assertEquals(BigDecimal("1e-7").toPlainString(), expr.eval("1E-7").toPlainString())
        assertEquals(BigDecimal(".101e+2").toPlainString(), expr.eval(".101e+2").toPlainString())
        assertEquals(BigDecimal(".123e2").toPlainString(), expr.eval(".123e2").toPlainString())
        assertEquals(BigDecimal("3212.123e-2").toPlainString(), expr.eval("3212.123e-2").toPlainString())
    }

    @Test
    fun `test normal expression`() {
        val expr = Expressions()
        assertEquals(BigDecimal(".123e2").add(BigDecimal("3212.123e-2")).toPlainString(),
                     expr.eval(".123e2+3212.123e-2").toPlainString())
        assertEquals(BigDecimal("1e+7").minus(BigDecimal("52132e-2")).toPlainString(),
                     expr.eval("1E+7-52132e-2").toPlainString())
    }
}