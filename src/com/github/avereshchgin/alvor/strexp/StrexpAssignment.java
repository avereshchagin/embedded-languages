package com.github.avereshchgin.alvor.strexp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StrexpAssignment implements StrexpNode {

    private final List<StrexpNode> childNodes = new ArrayList<StrexpNode>();

    private String variableName;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void joinNode(StrexpNode childNode) {
        childNodes.add(childNode);
    }

    public <E> E accept(StringExpressionVisitor<E> visitor) {
        return visitor.visitRoot(this);
    }

    public List<StrexpNode> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    public String toString() {
        String result = (variableName != null ? variableName : "") + " <- ";
        for (StrexpNode node : childNodes) {
            result += node.toString();
        }
        return result;
    }
}
