package com.github.avereshchgin.alvor.regex;

public class RegexPlus extends RegexNode {

    private final RegexNode node;

    public RegexPlus(RegexNode node) {
        this.node = node;
    }
}
