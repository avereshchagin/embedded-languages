package com.github.avereshchgin.alvor.regex;

import java.util.List;

public class RegexPlus extends RegexNode {

    private RegexNode node;

    public List<String> getReferences() {
        return node.getReferences();
    }
}
