package com.github.avereshchagin.emblang.controlflow;

public class EntryInstruction implements Instruction {

    private final String name;

    public EntryInstruction(String name) {
        this.name = name;
    }

    public <E> E accept(InstructionVisitor<E> visitor) {
        return visitor.visitEntryInstruction(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
