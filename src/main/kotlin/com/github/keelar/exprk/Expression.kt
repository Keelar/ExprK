package com.github.keelar.exprk

import com.github.keelar.exprk.internal.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class ExpressionException internal constructor(message: String)
    : RuntimeException(message)

@Suppress("unused")
class Expression {

    private var mathContext = MathContext.DECIMAL64
    private val variables: LinkedHashMap<String, Expr> = linkedMapOf()

    val precision: Int
        get() = mathContext.precision

    val roundingMode: RoundingMode
        get() = mathContext.roundingMode

    fun setPrecision(precision: Int): Expression {
        mathContext = MathContext(precision, mathContext.roundingMode)

        return this
    }

    fun setRoundingMode(roundingMode: RoundingMode): Expression {
        mathContext = MathContext(mathContext.precision, roundingMode)

        return this
    }

    fun define(name: String, value: Long): Expression {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: Double): Expression {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: BigDecimal): Expression {
        define(name, value.toString())

        return this
    }

    fun define(name: String, expression: String): Expression {
        val expr = parse(expression)
        variables += name to expr

        return this
    }

    fun eval(expression: String): BigDecimal {
        val evaluator = Evaluator(mathContext)

        for ((name, expr) in variables) {
            evaluator.define(name, expr)
        }

        return evaluator.eval(parse(expression))
    }

    private fun parse(expression: String): Expr {
        return parse(scan(expression))
    }

    private fun parse(tokens: List<Token>): Expr {
        return Parser(tokens).parse()
    }

    private fun scan(expression: String): List<Token> {
        return Scanner(expression, mathContext).scanTokens()
    }

}