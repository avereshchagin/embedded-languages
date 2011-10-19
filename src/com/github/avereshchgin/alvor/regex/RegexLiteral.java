package com.github.avereshchgin.alvor.regex;

public class RegexLiteral extends RegexNode {

    private final String literal;

    public RegexLiteral(String literal) {
        this.literal = literal;
    }

    public String toString() {
        return "\"" + literal + "\"";
    }
}
