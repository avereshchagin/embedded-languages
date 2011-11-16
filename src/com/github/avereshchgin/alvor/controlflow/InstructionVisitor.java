package com.github.avereshchgin.alvor.controlflow;

public class InstructionVisitor<E> {

    public E visitEntryInstruction(EntryInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitAssignmentInstruction(AssignmentInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitConditionalInstruction(ConditionalInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitLoopInstruction(LoopInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitJumpInstruction(JumpInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitLabelTargetInstruction(LabelTargetInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitReturnInstruction(ReturnInstruction instruction) {
        return visitInstruction(instruction);
    }

    public E visitInstruction(Instruction instruction) {
        return null;
    }
}
