package com.github.avereshchagin.emblang.controlflow;

import com.github.avereshchagin.emblang.regex.RegexNode;
import com.github.avereshchagin.emblang.regex.RegexVariable;

/**
 * Class represents assignment of string as regular expression to variable.
 */
public class AssignmentInstruction implements Instruction {

    /**
     * Variable which is being modified.
     */
    private final RegexVariable variable;

    /**
     * Regular expression tree root for assigned value.
     */
    private final RegexNode expression;

    private boolean verificationRequired;

    public AssignmentInstruction(RegexVariable variable, RegexNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitAssignmentInstruction(this);
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
        return variable + " <- " + expression;
    }
}
