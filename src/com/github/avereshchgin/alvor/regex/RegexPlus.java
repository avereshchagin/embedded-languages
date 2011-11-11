package com.github.avereshchgin.alvor.regex;

public class RegexPlus implements RegexNode {

    private final RegexNode node;

    public RegexPlus(RegexNode node) {
        this.node = node;
    }

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitPlus(this);
    }

    @Override
    public String toString() {
        if (node != null) {
            return "(" + node.toString() + ")+";
        } else {
            return "\"\"";
        }
    }
}
