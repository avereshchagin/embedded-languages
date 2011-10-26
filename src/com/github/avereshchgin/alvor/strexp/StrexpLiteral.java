package com.github.avereshchgin.alvor.strexp;

public class StrexpLiteral implements StrexpNode {

    private final String literal;

    public StrexpLiteral(String literal) {
        this.literal = literal;
    }

    public void joinNode(StrexpNode node) {
    }

    public <E> E accept(StringExpressionVisitor<E> visitor) {
        return visitor.visitLiteral(this);
    }

    public String getLiteral() {
        return literal;
    }

    public String toString() {
        return "\"" + literal + "\"";
    }
}
