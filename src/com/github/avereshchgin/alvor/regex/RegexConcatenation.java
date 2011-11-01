package com.github.avereshchgin.alvor.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RegexConcatenation implements RegexNode {

    private final List<RegexNode> childNodes = new ArrayList<RegexNode>();

    public RegexConcatenation() {

    }

    public RegexConcatenation(RegexNode leftNode, RegexNode rightNode) {
        connectNode(leftNode);
        connectNode(rightNode);
    }

    public void connectNode(RegexNode node) {
        childNodes.add(node);
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitConcatenation(this);
    }

    public List<RegexNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        Iterator<RegexNode> iterator = childNodes.iterator();
        if (iterator.hasNext()) {
            result.append(iterator.next());
            while (iterator.hasNext()) {
                result.append(" ");
                result.append(iterator.next());
            }
        }
        result.append(")");
        return result.toString();
    }
}
