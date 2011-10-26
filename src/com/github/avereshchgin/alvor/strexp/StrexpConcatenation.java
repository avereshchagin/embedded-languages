package com.github.avereshchgin.alvor.strexp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StrexpConcatenation implements StrexpNode {

    private final List<StrexpNode> childNodes = new ArrayList<StrexpNode>();

    public StrexpConcatenation() {
    }

    public void joinNode(StrexpNode node) {
        childNodes.add(node);
    }

    public <E> E accept(StringExpressionVisitor<E> visitor) {
        return visitor.visitConcatenation(this);
    }

    public List<StrexpNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    public String toString() {
        String result = "";
        for (StrexpNode node : childNodes) {
            result += node + " ";
        }
        return result;
    }
}
