package com.github.avereshchagin.emblang.controlflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlow {

    /**
     * List of all instructions.
     */
    private final List<Instruction> instructions = new ArrayList<Instruction>();

    /**
     * List of entry instructions for all methods.
     */
    private final List<EntryInstruction> methodEntries = new ArrayList<EntryInstruction>();

    /**
     * Adds instruction after last instruction of current list.
     *
     * @param instruction An instruction to be added.
     */
    public void addLast(Instruction instruction) {
        if (instruction instanceof EntryInstruction) {
            methodEntries.add((EntryInstruction) instruction);
        }
        instructions.add(instruction);
    }

    /**
     * @return Unmodifiable list of all instructions.
     */
    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    /**
     * @return Unmodifiable list of entry instructions for all methods.
     */
    public List<EntryInstruction> getMethodEntries() {
        return Collections.unmodifiableList(methodEntries);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Instruction instruction : instructions) {
            stringBuilder.append(instruction.toString()).append('\n');
        }
        return stringBuilder.toString();
    }
}
