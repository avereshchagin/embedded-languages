package com.github.avereshchagin.emblang.regex;

public class RegexVariable implements RegexNode {

    private final String name;

    public RegexVariable(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            this.name = "tmp_" + System.identityHashCode(this);
        }
    }

    public void connectNode(RegexNode node) {
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitVariable(this);
    }

    public String toString() {
        return "<" + name + ">";
    }
}
