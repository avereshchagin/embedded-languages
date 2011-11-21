package com.github.avereshchagin.emblang.regex;

public class RegexAssignment implements RegexNode {

    private RegexNode childNode;

    private RegexVariable variable;

    public RegexVariable getVariable() {
        return variable;
    }

    public void setVariable(RegexVariable variable) {
        assert this.variable == null;
        this.variable = variable;
    }

    public void connectNode(RegexNode childNode) {
        assert this.childNode == null;
        this.childNode = childNode;
    }

    public <E> E accept(RegularExpressionVisitor<E> visitor) {
        return visitor.visitAssignment(this);
    }

    public RegexNode getChildNode() {
        return childNode;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (variable != null) {
            result.append(variable);
        }
        result.append(" <- ");
        if (childNode != null) {
            result.append(childNode);
        }
        return result.toString();
    }
}
