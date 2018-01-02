package com.github.keelar.exprk.internal

import java.math.BigDecimal

internal sealed class Expr {

    abstract fun <R> accept(visitor: ExprVisitor<R>): R

}

internal class BinaryExpr(val left: Expr,
                          val operator: Token,
                          val right: Expr) : Expr() {

    override fun <R> accept(visitor: ExprVisitor<R>): R {
        return visitor.visitBinaryExpr(this)
    }

}

internal class GroupingExpr(val expression: Expr) : Expr() {

    override fun <R> accept(visitor: ExprVisitor<R>): R {
        return visitor.visitGroupingExpr(this)
    }

}

internal class LiteralExpr(val value: BigDecimal) : Expr() {

    override fun <R> accept(visitor: ExprVisitor<R>): R {
        return visitor.visitLiteralExpr(this)
    }

}

internal class UnaryExpr(val operator: Token,
                         val right: Expr) : Expr() {

    override fun <R> accept(visitor: ExprVisitor<R>): R {
        return visitor.visitUnaryExpr(this)
    }

}

internal class VariableExpr(val name: Token) : Expr() {

    override fun <R> accept(visitor: ExprVisitor<R>): R {
        return visitor.visitVariableExpr(this)
    }

}

internal interface ExprVisitor<out R> {

    fun visitBinaryExpr(expr: BinaryExpr): R

    fun visitGroupingExpr(expr: GroupingExpr): R

    fun visitLiteralExpr(expr: LiteralExpr): R

    fun visitUnaryExpr(expr: UnaryExpr): R

    fun visitVariableExpr(expr: VariableExpr): R

}