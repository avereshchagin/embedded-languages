package com.github.avereshchagin.emblang.cfg;

import com.github.avereshchagin.emblang.controlflow.*;
import com.intellij.openapi.util.text.StringUtil;

import java.io.PrintStream;
import java.util.*;

public class CfgFromInstructionsBuilder implements CfgBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private final Map<Instruction, CfgStatement> nodes = new IdentityHashMap<Instruction, CfgStatement>();

    private final ControlFlow controlFlow;

    private Instruction previousInstruction = null;

    private CfgFromInstructionsBuilder(ControlFlow controlFlow) {
        this.controlFlow = controlFlow;
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

    public static CfgFromInstructionsBuilder fromControlFlow(ControlFlow controlFlow) {
        final CfgFromInstructionsBuilder builder = new CfgFromInstructionsBuilder(controlFlow);
        builder.buildGraph();
        return builder;
    }

    private void buildGraph() {
        for (Instruction instruction : controlFlow.getInstructions()) {
            instruction.accept(new InstructionVisitor<Object>() {

                @Override
                public Object visitEntryInstruction(EntryInstruction instruction) {
                    CfgRootStatement node = new CfgRootStatement(instruction.getName());
                    nodes.put(instruction, node);
                    cfg.addNode(node);
                    cfg.addMethod(node);
                    return null;
                }

                @Override
                public Object visitAssignmentInstruction(AssignmentInstruction instruction) {
                    CfgStatement node = new CfgAssignmentStatement(instruction.getVariable(), instruction.getExpression());
                    node.setVerificationRequired(instruction.isVerificationRequired());
                    nodes.put(instruction, node);
                    cfg.addNode(node);
                    return null;
                }

                @Override
                public Object visitReturnInstruction(ReturnInstruction instruction) {
                    CfgStatement node = new CfgReturnStatement();
                    nodes.put(instruction, node);
                    cfg.addNode(node);
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
                    CfgStatement node = new CfgStatement();
                    nodes.put(instruction, node);
                    cfg.addNode(node);
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
                        CfgStatement source = nodes.get(previousInstruction);
                        CfgStatement destination = nodes.get(getNextInstruction(instruction.getJumpLabel().getTarget()));
                        if (source != null && destination != null) {
                            cfg.addEdge(source, destination);
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
                    CfgStatement source = nodes.get(instruction);
                    CfgStatement destination = nodes.get(getNextInstruction(instruction.getBreakLabel().getTarget()));
                    if (source != null && destination != null) {
                        cfg.addEdge(source, destination);
                    }
                    visitInstruction(instruction);
                    return null;
                }

                @Override
                public Object visitConditionalInstruction(ConditionalInstruction instruction) {
                    CfgStatement source = nodes.get(instruction);
                    CfgStatement destination = nodes.get(getNextInstruction(instruction.getFalseLabel().getTarget()));
                    if (source != null && destination != null) {
                        cfg.addEdge(source, destination);
                    }
                    visitInstruction(instruction);
                    return null;
                }

                @Override
                public Object visitReturnInstruction(ReturnInstruction instruction) {
                    if (previousInstruction != null) {
                        CfgStatement source = nodes.get(previousInstruction);
                        CfgStatement destination = nodes.get(getNextInstruction(instruction));
                        if (source != null && destination != null) {
                            cfg.addEdge(source, destination);
                        }
                    }
                    previousInstruction = null;
                    return null;
                }

                @Override
                public Object visitInstruction(Instruction instruction) {
                    if (previousInstruction != null) {
                        CfgStatement source = nodes.get(previousInstruction);
                        CfgStatement destination = nodes.get(getNextInstruction(instruction));
                        if (source != null && destination != null) {
                            cfg.addEdge(source, destination);
                        }
                    }
                    previousInstruction = instruction;
                    return null;
                }
            });
        }
    }

    public void printDotGraph(PrintStream out) {
        out.println("digraph G {");
        out.println("node [style=filled];");
        for (CfgStatement node : cfg.getNodes()) {
            out.print(System.identityHashCode(node) + " [label=\"" + StringUtil.escapeQuotes(node.toString()) + "\",");
            if (node instanceof CfgRootStatement || node instanceof CfgReturnStatement) {
                out.print("color=lightblue");
            } else if (node.isVerificationRequired()) {
                out.print("color=red");
            } else if (!"".equals(node.toString())) {
                out.print("color=palegreen");
            } else {
                out.print("color=gray");
            }
            out.println("];");
        }
        for (CfgEdge edge : cfg.getEdges()) {
            out.println(System.identityHashCode(edge.getSource()) + " -> " +
                    System.identityHashCode(edge.getDestination()) +
                    (CfgEdge.Type.BACK.equals(edge.getType()) ? " [style=dashed]" : "") + ";");
        }
        out.println("}");
    }

    public void showGraph() {
        try {
            Process process = Runtime.getRuntime().exec("/usr/local/bin/dot -Tpng -o/tmp/graph.png");
            PrintStream out = new PrintStream(process.getOutputStream());
            printDotGraph(out);
            out.flush();
            out.close();
            process.waitFor();
            Runtime.getRuntime().exec("open /tmp/graph.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ControlFlowGraph getControlFlowGraph() {
        return cfg;
    }
}