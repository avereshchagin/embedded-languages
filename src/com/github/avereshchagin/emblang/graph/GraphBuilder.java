package com.github.avereshchagin.emblang.graph;

import com.github.avereshchagin.emblang.controlflow.*;

import java.util.*;

/**
 * @author A. Vereshchagin
 */
public class GraphBuilder {

    // TODO: optimize it
    private static Instruction getNextInstruction(Instruction current, List<Instruction> instructions, boolean followJumps) {
        return getNextInstruction(instructions.listIterator(instructions.indexOf(current)), instructions, followJumps);
    }

    private static Instruction getNextInstruction(ListIterator<Instruction> iterator,
                                                  final List<Instruction> instructions,
                                                  final boolean followJumps) {
        Instruction result = null;
        final List<ListIterator<Instruction>> it = new ArrayList<ListIterator<Instruction>>();
        it.add(iterator);
        while (it.get(0).hasNext()) {
            result = it.get(0).next();
            boolean shouldBreak = result.accept(new InstructionVisitor<Boolean>() {

                @Override
                public Boolean visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return false;
                }

                @Override
                public Boolean visitJumpInstruction(JumpInstruction instruction) {
                    if (followJumps) {
                        it.set(0, instructions.listIterator(instructions.indexOf(instruction.getJumpLabel().getTarget())));
                    }
                    return false;
                }

                @Override
                public Boolean visitInstruction(Instruction instruction) {
                    return true;
                }
            });
            if (shouldBreak) {
                break;
            }
        }
        return result;
    }

    public static Graph<Instruction> buildFromControlFlow(final ControlFlow controlFlow) {
        final Graph<Instruction> graph = new GraphImpl<Instruction>();
        final Map<Instruction, GraphNode> instructionNodeMap = new IdentityHashMap<Instruction, GraphNode>();

        // Adding nodes to a graph
        for (Instruction instruction : controlFlow.getInstructions()) {
            instruction.accept(new InstructionVisitor<Void>() {

                @Override
                public Void visitJumpInstruction(JumpInstruction instruction) {
                    return null;
                }

                @Override
                public Void visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return null;
                }

                @Override
                public Void visitInstruction(Instruction instruction) {
                    GraphNode node = graph.addNode(instruction);
                    instructionNodeMap.put(instruction, node);
                    return null;
                }
            });
        }

        // null instruction maps to null node
        instructionNodeMap.put(null, null);

        // Connecting nodes
        final Instruction[] previousInstruction = {null};
//        final ListIterator<Instruction> it = controlFlow.getInstructions().listIterator();
        for (Instruction instruction : controlFlow.getInstructions()) {
            instruction.accept(new InstructionVisitor<Void>() {

                @Override
                public Void visitConditionalInstruction(ConditionalInstruction instruction) {
                    GraphNode source = instructionNodeMap.get(instruction);
                    GraphNode destination = instructionNodeMap.get(
                            getNextInstruction(instruction.getFalseLabel().getTarget(), controlFlow.getInstructions(), true));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    return visitInstruction(instruction);
                }

                @Override
                public Void visitLoopInstruction(LoopInstruction instruction) {
                    GraphNode source = instructionNodeMap.get(instruction);
                    GraphNode destination = instructionNodeMap.get(
                            getNextInstruction(instruction.getBreakLabel().getTarget(), controlFlow.getInstructions(), true));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    return visitInstruction(instruction);
                }

                @Override
                public Void visitJumpInstruction(JumpInstruction instruction) {
                    GraphNode source = instructionNodeMap.get(previousInstruction[0]);
                    GraphNode destination = instructionNodeMap.get(
                            getNextInstruction(instruction.getJumpLabel().getTarget(), controlFlow.getInstructions(), true));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    previousInstruction[0] = null;
                    return null;
                }

                @Override
                public Void visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return null;
                }

                @Override
                public Void visitReturnInstruction(ReturnInstruction instruction) {
                    visitInstruction(instruction);
                    previousInstruction[0] = null;
                    return null;
                }

                @Override
                public Void visitInstruction(Instruction instruction) {
                    GraphNode source = instructionNodeMap.get(previousInstruction[0]);
                    GraphNode destination = instructionNodeMap.get(
                            getNextInstruction(instruction, controlFlow.getInstructions(), true));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    previousInstruction[0] = instruction;
                    return null;
                }
            });
        }

        return graph;
    }
}
