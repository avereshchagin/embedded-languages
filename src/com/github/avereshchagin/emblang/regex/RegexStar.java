package com.github.avereshchagin.emblang.regex;

import java.util.Set;

public class RegexStar implements RegexNode {

    private final RegexNode node;

    public RegexStar(RegexNode node) {
        this.node = node;
    }

    public void connectNode(RegexNode node) {
    }

    public RegexNode getChildNode() {
        return node;
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitStar(this);
    }

    public Set<RegexVariable> findUsedVariables() {
        return node.findUsedVariables();
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
