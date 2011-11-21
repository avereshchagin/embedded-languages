package com.github.avereshchagin.emblang.regex;

public class RegexStar implements RegexNode {

    private final RegexNode node;

    public RegexStar(RegexNode node) {
        this.node = node;
    }

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitStar(this);
    }

    @Override
    public String toString() {
        if (node != null) {
            return "(" + node.toString() + ")*";
        } else {
            return "\"\"";
        }
    }
}
