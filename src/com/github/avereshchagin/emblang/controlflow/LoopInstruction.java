package com.github.avereshchagin.emblang.controlflow;

public class LoopInstruction implements Instruction {

    private final Label continueLabel = new Label();

    private final Label breakLabel = new Label();

    public Label getContinueLabel() {
        return continueLabel;
    }

    public Label getBreakLabel() {
        return breakLabel;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitLoopInstruction(this);
    }

    @Override
    public String toString() {
        return "iffalse <> jump " + breakLabel.toString();
    }
}
