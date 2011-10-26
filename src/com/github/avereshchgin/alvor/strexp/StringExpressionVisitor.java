package com.github.avereshchgin.alvor.strexp;

public class StringExpressionVisitor<E> {

    public E visitConcatenation(StrexpConcatenation concatenation) {
        return visitAnyNode(concatenation);
    }

    public E visitLiteral(StrexpLiteral literal) {
        return visitAnyNode(literal);
    }

    public E visitVariable(StrexpVariable variable) {
        return visitAnyNode(variable);
    }

    public E visitRoot(StrexpRoot root) {
        return visitAnyNode(root);
    }

    public E visitAnyNode(StrexpNode node) {
        return null;
    }
}
