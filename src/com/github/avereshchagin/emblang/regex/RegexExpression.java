package com.github.avereshchagin.emblang.regex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegexExpression implements RegexNode {

    private final List<RegexNode> childNodes = new ArrayList<RegexNode>();

    public void connectNode(RegexNode node) {
        childNodes.add(node);
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitExpression(this);
    }

    public List<RegexNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RegexNode node : childNodes) {
            stringBuilder.append(node);
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }
}
