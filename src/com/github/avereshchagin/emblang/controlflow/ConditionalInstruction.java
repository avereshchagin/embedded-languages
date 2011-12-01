package com.github.avereshchagin.emblang.controlflow;

public class ConditionalInstruction implements Instruction {

    /**
     * Target label to jump if condition isn't satisfied.
     */
    private final Label falseLabel = new Label();

    /**
     * Label to instruction where conditional branches are merged.
     */
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
        return "iffalse " + falseLabel.toString();
    }
}
