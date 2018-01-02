package com.github.keelar.exprk.internal

import com.github.keelar.exprk.ExpressionException
import com.github.keelar.exprk.internal.TokenType.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

internal class Evaluator(private val mathContext: MathContext) : ExprVisitor<BigDecimal> {

    private val variables: LinkedHashMap<String, BigDecimal> = linkedMapOf()

    fun define(name: String, expr: Expr): Evaluator {
        variables += name to eval(expr)

        return this
    }

    fun eval(expr: Expr): BigDecimal {
        return expr.accept(this)
    }

    private fun BigDecimal.pow(n: BigDecimal): BigDecimal {
        var right = n
        val signOfRight = right.signum()
        right = right.multiply(signOfRight.toBigDecimal())
        val remainderOfRight = right.remainder(BigDecimal.ONE)
        val n2IntPart = right.subtract(remainderOfRight)
        val intPow = pow(n2IntPart.intValueExact(), mathContext)
        val doublePow = BigDecimal(Math
                .pow(toDouble(), remainderOfRight.toDouble()))

        var result = intPow.multiply(doublePow, mathContext)
        if (signOfRight == -1) result = BigDecimal
                .ONE.divide(result, mathContext.precision, RoundingMode.HALF_UP)

        return result
    }

    override fun visitBinaryExpr(expr: BinaryExpr): BigDecimal {
        val left = eval(expr.left)
        val right = eval(expr.right)

        return when (expr.operator.type) {
            PLUS -> left.plus(right)
            MINUS -> left.minus(right)
            STAR -> left.times(right)
            SLASH -> left.divide(right, mathContext)
            EXPONENT -> left.pow(right)
            else -> throw ExpressionException(
                    "Invalid binary operator '${expr.operator.lexeme}'")
        }
    }

    override fun visitGroupingExpr(expr: GroupingExpr): BigDecimal {
        return eval(expr.expression)
    }

    override fun visitLiteralExpr(expr: LiteralExpr): BigDecimal {
        return expr.value
    }

    override fun visitUnaryExpr(expr: UnaryExpr): BigDecimal {
        val right = eval(expr.right)

        return when (expr.operator.type) {
            MINUS -> {
                right.negate()
            }
            else -> throw ExpressionException("Invalid unary operator")
        }
    }

    override fun visitVariableExpr(expr: VariableExpr): BigDecimal {
        val name = expr.name.lexeme

        return variables[name] ?:
                throw ExpressionException("Undefined variable '$name'")
    }

}