package com.github.avereshchgin.alvor.controlflow;

public interface Instruction {

    public <E> E accept(InstructionVisitor<E> visitor);
}
