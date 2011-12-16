package com.github.avereshchagin.emblang.graph;

import com.github.avereshchagin.emblang.controlflow.*;

import java.util.*;

/**
 * Author: A. Vereshchagin
 * Date: 16.12.11
 */
public class GraphFromControlFlowBuilder implements GraphBuilder {

    private final Graph<NodeData> graph = new Graph<NodeData>();
    private final ControlFlow controlFlow;
    private final Map<Instruction, Node<NodeData>> nodes = new IdentityHashMap<Instruction, Node<NodeData>>();
    private Instruction previousInstruction = null;

    public GraphFromControlFlowBuilder(ControlFlow controlFlow) {
        this.controlFlow = controlFlow;
        buildGraph();
    }

    private Instruction skipEmptyInstructions(Iterator<Instruction> it) {
        Instruction result = null;
        while (it.hasNext()) {
            result = it.next();
            boolean shouldBreak = result.accept(new InstructionVisitor<Boolean>() {

                @Override
                public Boolean visitLabelTargetInstruction(LabelTargetInstruction instruction) {
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

    private Instruction getNextInstruction(Instruction start) {
        Instruction result = null;
        final List<Instruction> instructions = controlFlow.getInstructions();
        final List<ListIterator<Instruction>> it = new ArrayList<ListIterator<Instruction>>();
        it.add(instructions.listIterator(instructions.indexOf(start)));
        while (it.get(0).hasNext()) {
            result = it.get(0).next();
            boolean shouldBreak = result.accept(new InstructionVisitor<Boolean>() {

                @Override
                public Boolean visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return false;
                }

                @Override
                public Boolean visitJumpInstruction(JumpInstruction instruction) {
                    it.set(0, instructions.listIterator(instructions.indexOf(instruction.getJumpLabel().getTarget())));
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

    private void buildGraph() {
        for (Instruction instruction : controlFlow.getInstructions()) {
            instruction.accept(new InstructionVisitor<Object>() {

                @Override
                public Object visitEntryInstruction(EntryInstruction instruction) {
//                    CfgRootStatement node = new CfgRootStatement(instruction.getName());
                    Node<NodeData> node = new Node<NodeData>();
                    node.setData(new NodeData(null, null));
                    nodes.put(instruction, node);
                    graph.addNode(node);
//                    graph.addMethod(node);
                    return null;
                }

                @Override
                public Object visitAssignmentInstruction(AssignmentInstruction instruction) {
                    NodeData nodeData = new NodeData(instruction.getVariable(), instruction.getExpression());
                    nodeData.setVerificationRequired(instruction.isVerificationRequired());
                    Node<NodeData> node = new Node<NodeData>();
                    node.setData(nodeData);
                    nodes.put(instruction, node);
                    graph.addNode(node);
                    return null;
                }

                @Override
                public Object visitReturnInstruction(ReturnInstruction instruction) {
                    Node<NodeData> node = new Node<NodeData>();
                    node.setData(new NodeData(null, null));
                    nodes.put(instruction, node);
                    graph.addNode(node);
                    return null;
                }

                @Override
                public Object visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return null;
                }

                @Override
                public Object visitJumpInstruction(JumpInstruction instruction) {
                    return null;
                }

                @Override
                public Object visitInstruction(Instruction instruction) {
                    Node<NodeData> node = new Node<NodeData>();
                    node.setData(new NodeData(null, null));
                    nodes.put(instruction, node);
                    graph.addNode(node);
                    return null;
                }
            });
        }

        previousInstruction = null;
        final Iterator<Instruction> it = controlFlow.getInstructions().iterator();
        while (it.hasNext()) {
            skipEmptyInstructions(it).accept(new InstructionVisitor<Object>() {

                @Override
                public Object visitJumpInstruction(JumpInstruction instruction) {
                    if (previousInstruction != null) {
                        Node<NodeData> source = nodes.get(previousInstruction);
                        Node<NodeData> destination = nodes.get(getNextInstruction(instruction.getJumpLabel().getTarget()));
                        if (source != null && destination != null) {
                            graph.addEdge(source, destination);
                        }
                        previousInstruction = null;
                    }
                    return null;
                }

                @Override
                public Object visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return null;
                }

                @Override
                public Object visitLoopInstruction(LoopInstruction instruction) {
                    Node<NodeData> source = nodes.get(instruction);
                    Node<NodeData> destination = nodes.get(getNextInstruction(instruction.getBreakLabel().getTarget()));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    visitInstruction(instruction);
                    return null;
                }

                @Override
                public Object visitConditionalInstruction(ConditionalInstruction instruction) {
                    Node<NodeData> source = nodes.get(instruction);
                    Node<NodeData> destination = nodes.get(getNextInstruction(instruction.getFalseLabel().getTarget()));
                    if (source != null && destination != null) {
                        graph.addEdge(source, destination);
                    }
                    visitInstruction(instruction);
                    return null;
                }

                @Override
                public Object visitReturnInstruction(ReturnInstruction instruction) {
                    if (previousInstruction != null) {
                        Node<NodeData> source = nodes.get(previousInstruction);
                        Node<NodeData> destination = nodes.get(getNextInstruction(instruction));
                        if (source != null && destination != null) {
                            graph.addEdge(source, destination);
                        }
                    }
                    previousInstruction = null;
                    return null;
                }

                @Override
                public Object visitInstruction(Instruction instruction) {
                    if (previousInstruction != null) {
                        Node<NodeData> source = nodes.get(previousInstruction);
                        Node<NodeData> destination = nodes.get(getNextInstruction(instruction));
                        if (source != null && destination != null) {
                            graph.addEdge(source, destination);
                        }
                    }
                    previousInstruction = instruction;
                    return null;
                }
            });
        }
    }

    public Graph<NodeData> getGraph() {
        return graph;
    }
}
