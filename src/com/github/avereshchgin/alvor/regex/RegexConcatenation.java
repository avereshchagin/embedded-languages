package com.github.avereshchgin.alvor.regex;

public class RegexConcatenation extends RegexNode {

    private final RegexNode leftNode;
    private final RegexNode rightNode;

    public RegexConcatenation(RegexNode leftNode, RegexNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString() {
        return "(" + leftNode + " " + rightNode + ")";
    }
}
