package com.github.keelar.exprk.internal

import java.math.MathContext
import com.github.keelar.exprk.ExpressionException
import com.github.keelar.exprk.internal.TokenType.*

internal class Scanner(private val source: String,
                       private val mathContext: MathContext) {

    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            scanToken()
        }

        tokens.add(Token(EOF, "", null))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        start = current
        val c = advance()

        when (c) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            '*' -> addToken(STAR)
            '/' -> addToken(SLASH)
            '%' -> addToken(MODULO)
            '^' -> addToken(EXPONENT)
            ' ',
            '\r',
            '\t' -> {
                // Ignore whitespace.
            }
            else -> {
                when {
                    c.isDigit() -> number()
                    c.isAlpha() -> identifier()
                    else -> ExpressionException("Unexpected character '$c'")
                }
            }
        }
    }

    private fun number() {
        while (peek().isDigit()) advance()

        if (peek() == '.' && peekNext().isDigit()) {
            advance()

            while (peek().isDigit()) advance()
        }

        val value = source
                .substring(start, current)
                .toBigDecimal(mathContext)

        addToken(NUMBER, value)
    }

    private fun identifier() {
        while (peek().isAlphaNumeric()) advance()

        addToken(IDENTIFIER)
    }

    private fun advance() = source[current++]

    private fun peek(): Char {
        return if (isAtEnd()) {
            '\u0000'
        } else {
            source[current]
        }
    }

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) {
            '\u0000'
        } else {
            source[current + 1]
        }
    }

    private fun addToken(type: TokenType) = addToken(type, null)

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal))
    }

    private fun Char.isAlphaNumeric() = isAlpha() || isDigit()

    private fun Char.isAlpha() = this in 'a'..'z'
            || this in 'A'..'Z'
            || this == '_'

    private fun Char.isDigit() = this in '0'..'9'

}