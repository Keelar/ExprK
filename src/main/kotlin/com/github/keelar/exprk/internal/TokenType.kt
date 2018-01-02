package com.github.keelar.exprk.internal

internal enum class TokenType {

    // Single char tokens
    PLUS,
    MINUS,
    STAR,
    SLASH,
    EXPONENT,
    LEFT_PAREN,
    RIGHT_PAREN,

    // Literals
    NUMBER,
    IDENTIFIER,

    EOF

}