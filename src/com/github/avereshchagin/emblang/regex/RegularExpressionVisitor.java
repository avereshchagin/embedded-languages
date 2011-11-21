package com.github.avereshchagin.emblang.regex;

public class RegularExpressionVisitor<E> {

    public E visitConcatenation(RegexConcatenation concatenation) {
        return visitAnyNode(concatenation);
    }

    public E visitLiteral(RegexLiteral literal) {
        return visitAnyNode(literal);
    }

    public E visitVariable(RegexVariable variable) {
        return visitAnyNode(variable);
    }

    public E visitAlternation(RegexAlternation alternation) {
        return visitAnyNode(alternation);
    }

    public E visitEmpty(RegexEmpty empty) {
        return visitAnyNode(empty);
    }

    public E visitRange(RegexRange range) {
        return visitAnyNode(range);
    }

    public E visitAssignment(RegexAssignment assignment) {
        return visitAnyNode(assignment);
    }

    public E visitExpression(RegexExpression expression) {
        return visitAnyNode(expression);
    }

    public E visitStar(RegexStar star) {
        return visitAnyNode(star);
    }

    public E visitPlus(RegexPlus plus) {
        return visitAnyNode(plus);
    }

    public E visitAnyNode(RegexNode node) {
        return null;
    }
}
