package com.github.avereshchgin.alvor.controlflow;

public class ReturnInstruction implements Instruction {

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitReturnInstruction(this);
    }

    @Override
    public String toString() {
        return "return";
    }
}
