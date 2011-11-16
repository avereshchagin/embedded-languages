package com.github.avereshchgin.alvor.controlflow;

public class LabelTargetInstruction implements Instruction {

    private final Label label;

    public LabelTargetInstruction(Label label) {
        this.label = label;
        label.setTarget(this);
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitLabelTargetInstruction(this);
    }

    @Override
    public String toString() {
        return label + ":";
    }
}
