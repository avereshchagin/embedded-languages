package com.github.avereshchgin.alvor.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RegexAlternation implements RegexNode {

    private final List<RegexNode> childNodes = new ArrayList<RegexNode>();

    public RegexAlternation() {

    }

    public RegexAlternation(RegexNode leftNode, RegexNode rightNode) {
        childNodes.add(leftNode);
        childNodes.add(rightNode);
    }

    public void connectNode(RegexNode node) {
        childNodes.add(node);
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitAlternation(this);
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
                result.append(" | ");
                result.append(iterator.next());
            }
        }
        result.append(")");
        return result.toString();
    }
}
