package com.github.avereshchagin.emblang.controlflow.tests;

import com.github.avereshchagin.emblang.controlflow.*;
import com.github.avereshchagin.emblang.regex.RegexEmpty;
import com.github.avereshchagin.emblang.regex.RegexVariable;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ControlFlowTest {
    @Test
    public void testGetInstructions() throws Exception {
        List<Instruction> instructions = new ArrayList<Instruction>();
        instructions.add(new EntryInstruction("foo"));
        instructions.add(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        instructions.add(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        instructions.add(new AssignmentInstruction(new RegexVariable("c"), new RegexEmpty()));
        instructions.add(new ReturnInstruction());
        instructions.add(new EntryInstruction("bar"));
        instructions.add(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        instructions.add(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        instructions.add(new ReturnInstruction());
        ControlFlow controlFlow = new ControlFlow();
        for (Instruction instruction : instructions) {
            controlFlow.addLast(instruction);
        }
        Assert.assertEquals(instructions, controlFlow.getInstructions());
    }

    @Test
    public void testGetMethodEntries() throws Exception {
        EntryInstruction foo = new EntryInstruction("foo");
        EntryInstruction bar = new EntryInstruction("bar");
        ControlFlow controlFlow = new ControlFlow();
        controlFlow.addLast(foo);
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("c"), new RegexEmpty()));
        controlFlow.addLast(new ReturnInstruction());
        controlFlow.addLast(bar);
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        controlFlow.addLast(new ReturnInstruction());
        List<EntryInstruction> methodEntries = controlFlow.getMethodEntries();
        Assert.assertEquals(methodEntries.size(), 2);
        Assert.assertTrue(methodEntries.contains(foo));
        Assert.assertTrue(methodEntries.contains(bar));
    }
}
