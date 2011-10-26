package com.github.avereshchgin.alvor.regex;

public class RegexStar extends RegexNode {

    private final RegexNode node;

    public RegexStar(RegexNode node) {
        this.node = node;
    }
}
