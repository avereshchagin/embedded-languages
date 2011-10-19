package com.github.avereshchgin.alvor.regex;

public class RegexAlternation extends RegexNode {

    private RegexNode leftNode;
    private RegexNode rightNode;

    public RegexAlternation(RegexNode leftNode, RegexNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString() {
        return "(" + leftNode + "|" + rightNode + ")";
    }
}
