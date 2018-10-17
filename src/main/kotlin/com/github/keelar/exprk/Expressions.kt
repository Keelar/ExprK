package com.github.keelar.exprk

import com.github.keelar.exprk.internal.*
import com.github.keelar.exprk.internal.Function
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class ExpressionException(message: String)
    : RuntimeException(message)

@Suppress("unused")
class Expressions {

    private var mathContext = MathContext.DECIMAL64
    private val variables: LinkedHashMap<String, Expr> = linkedMapOf()
    private val functions: MutableMap<String, Function> = mutableMapOf()

    init {
        define("pi", Math.PI)
        define("e", Math.E)

        addFunction("abs", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1) throw ExpressionException(
                        "abs requires one argument")

                return arguments.first().abs()
            }
        })

        addFunction("sum", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.isEmpty()) throw ExpressionException(
                        "sum requires at least one argument")

                return arguments.reduce { sum, bigDecimal ->
                    sum.add(bigDecimal)
                }
            }
        })

        addFunction("floor", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1) throw ExpressionException(
                        "abs requires one argument")

                return arguments.first().setScale(0, RoundingMode.FLOOR)
            }
        })

        addFunction("ceil", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size != 1) throw ExpressionException(
                        "abs requires one argument")

                return arguments.first().setScale(0, RoundingMode.CEILING)
            }
        })

        addFunction("round", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.size !in listOf(1, 2)) throw ExpressionException(
                        "round requires either one or two arguments")

                val value = arguments.first()
                val scale = if (arguments.size == 2) arguments.last().toInt() else 0

                return value.setScale(scale, mathContext.roundingMode)
            }
        })

        addFunction("min", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.isEmpty()) throw ExpressionException(
                        "min requires at least one argument")

                return arguments.min()!!
            }
        })

        addFunction("max", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                if (arguments.isEmpty()) throw ExpressionException(
                        "max requires at least one argument")

                return arguments.max()!!
            }
        })

        addFunction("if", object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                val condition = arguments[0]
                val thenValue = arguments[1]
                val elseValue = arguments[2]

                return if (condition != BigDecimal.ZERO) {
                    thenValue
                } else {
                    elseValue
                }
            }
        })
    }

    val precision: Int
        get() = mathContext.precision

    val roundingMode: RoundingMode
        get() = mathContext.roundingMode

    fun setPrecision(precision: Int): Expressions {
        mathContext = MathContext(precision, mathContext.roundingMode)

        return this
    }

    fun setRoundingMode(roundingMode: RoundingMode): Expressions {
        mathContext = MathContext(mathContext.precision, roundingMode)

        return this
    }

    fun define(name: String, value: Long): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: Double): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, value: BigDecimal): Expressions {
        define(name, value.toString())

        return this
    }

    fun define(name: String, expression: String): Expressions {
        val expr = parse(expression)
        variables += name to expr

        return this
    }

    fun addFunction(name: String, function: Function): Expressions {
        functions += name to function

        return this
    }

    fun addFunction(name: String, func: (List<BigDecimal>) -> BigDecimal): Expressions {
        functions += name to object : Function() {
            override fun call(arguments: List<BigDecimal>): BigDecimal {
                return func(arguments)
            }

        }

        return this
    }

    fun eval(expression: String): BigDecimal {
        val evaluator = Evaluator(mathContext)

        for ((name, function) in functions) {
            evaluator.addFunction(name, function)
        }

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