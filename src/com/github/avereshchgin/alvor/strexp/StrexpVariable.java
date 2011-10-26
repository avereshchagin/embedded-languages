package com.github.avereshchgin.alvor.strexp;

public class StrexpVariable implements StrexpNode {

    private final String name;

    public StrexpVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void joinNode(StrexpNode node) {
    }

    public <E> E accept(StringExpressionVisitor<E> visitor) {
        return visitor.visitVariable(this);
    }

    public String toString() {
        return "<" + name + ">";
    }
}
