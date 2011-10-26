package com.github.avereshchgin.alvor.strexp;

public class StrexpEmpty implements StrexpNode {

    public void joinNode(StrexpNode node) {
    }

    public <E> E accept(StringExpressionVisitor<E> visitor) {
        return visitor.visitAnyNode(this);
    }

    public String toString() {
        return "\"\"";
    }
}
