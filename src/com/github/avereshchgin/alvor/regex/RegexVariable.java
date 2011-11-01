package com.github.avereshchgin.alvor.regex;

public class RegexVariable implements RegexNode {

    private final String name;

    private final int identity;

    public RegexVariable(String name, int identity) {
        this.name = name;
        this.identity = identity;
    }

    public void connectNode(RegexNode node) {
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RegexVariable) {
            RegexVariable that = (RegexVariable) o;
            return this.identity == that.identity;
        }
        return false;
    }

    public String toString() {
        return "<" + name + ">";
    }
}
