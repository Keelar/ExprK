package com.github.keelar.exprk

import org.junit.Test

class TestValidateExpression {

    @Test
    fun `test normal expression`() {
        Expressions().validateExpression("x + y", listOf("x", "y"))
    }

    @Test(expected = ExpressionException::class)
    fun `test expression with unknown variable`() {
        Expressions().validateExpression("x + y + z", listOf("x", "y"))
    }

    @Test(expected = ExpressionException::class)
    fun `test invalid syntax`() {
        Expressions().validateExpression("x + y)", listOf("x", "y"))
    }

    @Test
    fun `test expression with numbers`() {
        Expressions().setPrecision(5)
            .validateExpression("(46.0 + 23) + 1" ,listOf())
    }
}