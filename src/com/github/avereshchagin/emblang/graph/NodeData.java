package com.github.avereshchagin.emblang.graph;

import com.github.avereshchagin.emblang.regex.RegexNode;
import com.github.avereshchagin.emblang.regex.RegexVariable;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class NodeData {

    private boolean verificationRequired;

    /**
     * Variable which is being modified.
     */
    private final RegexVariable variable;

    /**
     * Regular expression tree root for assigned value.
     */
    private final RegexNode expression;

    public NodeData(RegexVariable variable, RegexNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public RegexVariable getVariable() {
        return variable;
    }

    public RegexNode getExpression() {
        return expression;
    }

    public boolean isVerificationRequired() {
        return verificationRequired;
    }

    public void setVerificationRequired(boolean verificationRequired) {
        this.verificationRequired = verificationRequired;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (variable != null) {
            stringBuilder.append(variable);
        }
        stringBuilder.append(" <- ");
        if (expression != null) {
            stringBuilder.append(expression);
        }
        return stringBuilder.toString();
    }
}
