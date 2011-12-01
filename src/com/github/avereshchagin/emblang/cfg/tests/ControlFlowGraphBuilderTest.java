package com.github.avereshchagin.emblang.cfg.tests;

import com.github.avereshchagin.emblang.cfg.CfgEdge;
import com.github.avereshchagin.emblang.cfg.CfgStatement;
import com.github.avereshchagin.emblang.cfg.ControlFlowGraph;
import com.github.avereshchagin.emblang.cfg.ControlFlowGraphBuilder;
import com.github.avereshchagin.emblang.controlflow.*;
import com.github.avereshchagin.emblang.regex.RegexEmpty;
import com.github.avereshchagin.emblang.regex.RegexVariable;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

public class ControlFlowGraphBuilderTest {

    @Test
    public void testLinearControlFlow() {
        ControlFlow controlFlow = new ControlFlow();
        controlFlow.addLast(new EntryInstruction("foo"));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("c"), new RegexEmpty()));
        controlFlow.addLast(new ReturnInstruction());
        ControlFlowGraphBuilder builder = ControlFlowGraphBuilder.fromControlFlow(controlFlow);
        ControlFlowGraph cfg = builder.getControlFlowGraph();
        List<CfgStatement> nodes = cfg.getNodes();
        Assert.assertTrue(nodes.size() == 5);
        List<CfgEdge> edges = cfg.getEdges();
        Assert.assertTrue(edges.size() == 4);
    }

    @Test
    public void testConditionalControlFlow() {
        ControlFlow controlFlow = new ControlFlow();
        controlFlow.addLast(new EntryInstruction("foo"));

        ConditionalInstruction ifInstruction = new ConditionalInstruction();
        controlFlow.addLast(ifInstruction);
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("a"), new RegexEmpty()));
        controlFlow.addLast(new JumpInstruction(ifInstruction.getEndLabel()));
        controlFlow.addLast(new LabelTargetInstruction(ifInstruction.getFalseLabel()));
        controlFlow.addLast(new AssignmentInstruction(new RegexVariable("b"), new RegexEmpty()));
        controlFlow.addLast(new LabelTargetInstruction(ifInstruction.getEndLabel()));

        controlFlow.addLast(new ReturnInstruction());

        ControlFlowGraphBuilder builder = ControlFlowGraphBuilder.fromControlFlow(controlFlow);
        ControlFlowGraph cfg = builder.getControlFlowGraph();
        List<CfgStatement> nodes = cfg.getNodes();
        Assert.assertTrue(nodes.size() == 5);
        List<CfgEdge> edges = cfg.getEdges();
        Assert.assertTrue(edges.size() == 5);
    }
}
