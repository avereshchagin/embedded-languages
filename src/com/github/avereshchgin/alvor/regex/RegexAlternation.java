package com.github.avereshchgin.alvor.regex;

public class RegexAlternation extends RegexPolyadic implements RegexNode {

    public RegexAlternation(RegexNode... nodes) {
        super(nodes);
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitAlternation(this);
    }

    public String toString() {
        return toString('|');
    }
}
