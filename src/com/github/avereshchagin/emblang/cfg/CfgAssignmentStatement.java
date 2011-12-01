package com.github.avereshchagin.emblang.cfg;

import com.github.avereshchagin.emblang.regex.RegexNode;
import com.github.avereshchagin.emblang.regex.RegexVariable;

/**
 * Author: A. Vereshchagin
 * Date: 01.12.11
 */
public class CfgAssignmentStatement extends CfgStatement {

    /**
     * Variable which is being modified.
     */
    private final RegexVariable variable;

    /**
     * Regular expression tree root for assigned value.
     */
    private final RegexNode expression;

    public CfgAssignmentStatement(RegexVariable variable, RegexNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public RegexVariable getVariable() {
        return variable;
    }

    public RegexNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(variable).append(" <- ").append(expression).toString();
    }
}
