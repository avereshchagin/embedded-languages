package com.github.avereshchgin.alvor.controlflow;

public class ConditionalInstruction implements Instruction {

    private final Label falseLabel = new Label();

    private final Label endLabel = new Label();

    public Label getEndLabel() {
        return endLabel;
    }

    public Label getFalseLabel() {
        return falseLabel;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitConditionalInstruction(this);
    }

    @Override
    public String toString() {
        return "iffalse <> jump " + falseLabel.toString();
    }
}
