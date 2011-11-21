package com.github.avereshchagin.emblang.regex;

public class RegexVariable implements RegexNode {

    private final String name;

    public RegexVariable(String name) {
        this.name = name;
    }

    public void connectNode(RegexNode node) {
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitVariable(this);
    }

    public String toString() {
        if (name != null && !name.isEmpty()) {
            return "<" + name + ">";
        } else {
            return "<tmp_" + System.identityHashCode(this) + ">";
        }
    }
}
