package com.github.avereshchgin.alvor.controlflow;

public class EntryInstruction implements Instruction {

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitEntryInstruction(this);
    }
}
