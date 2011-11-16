package com.github.avereshchgin.alvor.cfg;

import com.github.avereshchgin.alvor.controlflow.*;
import com.intellij.openapi.util.text.StringUtil;

import java.io.PrintStream;
import java.util.*;

public class ControlFlowGraphBuilder {

    private final ControlFlowGraph cfg = new ControlFlowGraph();

    private final Map<Instruction, CfgStatement> nodes = new IdentityHashMap<Instruction, CfgStatement>();

    private final ControlFlow controlFlow;

    private Instruction previousInstruction = null;

    private Instruction currentInstruction = null;

    private static enum PreviousEdgeOperation {
        UPDATE, SKIP, RESET
    }

    public ControlFlowGraphBuilder(ControlFlow controlFlow) {
        this.controlFlow = controlFlow;
    }

    public Instruction skipEmptyInstructions(Iterator<Instruction> it) {
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

    public Instruction getNextInstruction(Instruction start) {
        Instruction result = null;
        List<Instruction> instructions = controlFlow.getInstructions();
        ListIterator<Instruction> it = instructions.listIterator(instructions.indexOf(start));
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

    public static ControlFlowGraphBuilder fromControlFlow(ControlFlow controlFlow) {
        final ControlFlowGraphBuilder builder = new ControlFlowGraphBuilder(controlFlow);
        for (Instruction instruction : controlFlow.getInstructions()) {
            instruction.accept(new InstructionVisitor<Object>() {

                @Override
                public Object visitEntryInstruction(EntryInstruction instruction) {
                    // TODO: pass valid method name
                    CfgStatement node = new CfgRootStatement("");
                    builder.nodes.put(instruction, node);
                    builder.cfg.addNode(node);
                    return null;
                }

                @Override
                public Object visitReturnInstruction(ReturnInstruction instruction) {
                    CfgStatement node = new CfgReturnStatement();
                    builder.nodes.put(instruction, node);
                    builder.cfg.addNode(node);
                    return null;
                }

                @Override
                public Object visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    return null;
                }

                @Override
                public Object visitInstruction(Instruction instruction) {
                    // TODO: fix passing expression
                    CfgStatement node = new CfgRegularStatement(instruction.toString());
                    builder.nodes.put(instruction, node);
                    builder.cfg.addNode(node);
                    return null;
                }
            });
        }

        final Iterator<Instruction> it = controlFlow.getInstructions().iterator();
        while (it.hasNext()) {
            builder.currentInstruction = builder.skipEmptyInstructions(it);
            PreviousEdgeOperation operation = builder.currentInstruction.accept(new InstructionVisitor<PreviousEdgeOperation>() {

                @Override
                public PreviousEdgeOperation visitJumpInstruction(JumpInstruction instruction) {
                    CfgStatement source = builder.nodes.get(instruction);
                    CfgStatement destination = builder.nodes.get(builder.getNextInstruction(instruction.getJumpLabel().getTarget()));
                    if (source != null && destination != null) {
                        builder.cfg.addEdge(source, destination);
                    } else {
                        System.out.println("Source: " + source);
                    }
                    return PreviousEdgeOperation.RESET;
                }

                @Override
                public PreviousEdgeOperation visitLabelTargetInstruction(LabelTargetInstruction instruction) {
                    builder.currentInstruction = builder.skipEmptyInstructions(it);
                    return PreviousEdgeOperation.UPDATE; // was SKIP
                }

                @Override
                public PreviousEdgeOperation visitLoopInstruction(LoopInstruction instruction) {
                    CfgStatement source = builder.nodes.get(instruction);
                    CfgStatement destination = builder.nodes.get(builder.getNextInstruction(instruction.getBreakLabel().getTarget()));
                    if (source != null && destination != null) {
                        builder.cfg.addEdge(source, destination);
                    }
                    return PreviousEdgeOperation.UPDATE;
                }

                @Override
                public PreviousEdgeOperation visitConditionalInstruction(ConditionalInstruction instruction) {
                    CfgStatement source = builder.nodes.get(instruction);
                    CfgStatement destination = builder.nodes.get(builder.getNextInstruction(instruction.getFalseLabel().getTarget()));
                    if (source != null && destination != null) {
                        builder.cfg.addEdge(source, destination);
                    }
                    return PreviousEdgeOperation.UPDATE;
                }

                @Override
                public PreviousEdgeOperation visitReturnInstruction(ReturnInstruction instruction) {
                    return PreviousEdgeOperation.RESET;
                }

                @Override
                public PreviousEdgeOperation visitInstruction(Instruction instruction) {
                    return PreviousEdgeOperation.UPDATE;
                }
            });
            if (builder.previousInstruction != null) {
                CfgStatement source = builder.nodes.get(builder.previousInstruction);
                CfgStatement destination = builder.nodes.get(builder.getNextInstruction(builder.currentInstruction));
                if (source != null && destination != null) {
                    builder.cfg.addEdge(source, destination);
                }
            }
            switch (operation) {
                case UPDATE:
                    builder.previousInstruction = builder.getNextInstruction(builder.currentInstruction);
                    break;
                case RESET:
                    builder.previousInstruction = null;
                    break;
                case SKIP:
                    break;
            }
        }
        return builder;
    }

//    public void addMethod(PsiMethod psiMethod) {
//        CfgStatement rootNode = new CfgRootStatement(psiMethod.getName());
//        cfg.addNode(rootNode);
//        List<CfgStatement> prevNodes = CfgJavaElementVisitor.processCodeBlock(
//                this, Collections.singletonList(rootNode), psiMethod.getBody(),
//                Collections.<PsiIdentifier, CfgStatement>emptyMap()).getPreviousNodes();
//        CfgStatement returnNode = null;
//        for (CfgStatement prevNode : prevNodes) {
//            if (!(prevNode instanceof CfgReturnStatement)) {
//                if (returnNode == null) {
//                    returnNode = new CfgReturnStatement();
//                    cfg.addNode(returnNode);
//                }
//                cfg.addEdge(prevNode, returnNode);
//            }
//        }
//    }

    private void printDotGraph(PrintStream out) {
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
                    System.identityHashCode(edge.getDestination()) + ";");
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

//    public VerifiableMethodsFinder getFinder() {
//        return finder;
//    }
}
