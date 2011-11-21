package com.github.avereshchagin.emblang.controlflow;

public interface Instruction {

    public <E> E accept(InstructionVisitor<E> visitor);
}
