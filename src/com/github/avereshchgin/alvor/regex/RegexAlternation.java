package com.github.avereshchgin.alvor.regex;

import java.util.List;

public class RegexAlternation extends RegexNode {

    private RegexNode leftNode;
    private RegexNode rightNode;

    public RegexAlternation(RegexNode leftNode, RegexNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public List<String> getReferences() {
        List<String> references = leftNode.getReferences();
        references.addAll(rightNode.getReferences());
        return references;
    }

    public String toString() {
        return "(" + leftNode + "|" + rightNode + ")";
    }
}
