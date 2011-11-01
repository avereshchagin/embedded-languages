package com.github.avereshchgin.alvor.regex;

public class RegexLiteral implements RegexNode {

    private final String literal;

    public RegexLiteral(String literal) {
        this.literal = literal;
    }

    public void connectNode(RegexNode node) {

    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitLiteral(this);
    }

    public String getLiteral() {
        return literal;
    }

    public String toString() {
        return "\"" + literal + "\"";
    }
}
