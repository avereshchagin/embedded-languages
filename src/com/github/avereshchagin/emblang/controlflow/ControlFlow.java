package com.github.avereshchagin.emblang.controlflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlFlow {

    private final List<Instruction> instructions = new ArrayList<Instruction>();

//    private final List<Label> labels = new ArrayList<Label>();

    public void addLast(Instruction instruction) {
        instructions.add(instruction);
    }

//    public void addLabel(Label label) {
//        labels.add(label);
//    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Instruction instruction : instructions) {
            stringBuilder.append(instruction.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
