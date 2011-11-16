package com.github.avereshchgin.alvor.controlflow;

import com.github.avereshchgin.alvor.regex.RegexNode;
import com.github.avereshchgin.alvor.regex.RegexVariable;

public class AssignmentInstruction implements Instruction {

    private final RegexVariable variable;

    private final RegexNode expression;

    public AssignmentInstruction(RegexVariable variable, RegexNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitAssignmentInstruction(this);
    }

    @Override
    public String toString() {
        return variable + " <- " + expression;
    }
}
