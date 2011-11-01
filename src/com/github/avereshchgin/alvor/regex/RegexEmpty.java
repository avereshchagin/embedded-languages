package com.github.avereshchgin.alvor.regex;

public class RegexEmpty implements RegexNode {

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitEmpty(this);
    }

    public String toString() {
        return "\"\"";
    }
}
