package com.github.avereshchgin.alvor.controlflow;

public class JumpInstruction implements Instruction {

    private final Label jumpLabel;

    public JumpInstruction(Label jumpLabel) {
        this.jumpLabel = jumpLabel;
    }

    public Label getJumpLabel() {
        return jumpLabel;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitJumpInstruction(this);
    }

    @Override
    public String toString() {
        return "jump " + jumpLabel;
    }
}
