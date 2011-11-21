package com.github.avereshchagin.emblang.regex;

public class RegexConcatenation extends RegexPolyadic implements RegexNode {

    public RegexConcatenation(RegexNode... nodes) {
        super(nodes);
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitConcatenation(this);
    }

    public String toString() {
        return toString(' ');
    }
}
