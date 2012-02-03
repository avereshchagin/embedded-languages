package com.github.avereshchagin.emblang.regex;

import com.github.avereshchagin.emblang.controlflow.AssignmentInstruction;
import com.github.avereshchagin.emblang.controlflow.Instruction;
import com.github.avereshchagin.emblang.controlflow.InstructionVisitor;
import com.github.avereshchagin.emblang.graph.Graph;
import com.github.avereshchagin.emblang.graph.GraphNode;
import com.github.avereshchagin.emblang.graph.TransitiveClosure;
import com.github.avereshchagin.emblang.util.Tuple;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class RegularExpressionBuilder {

    private final Graph<Instruction> graph;

    private final TransitiveClosure transitiveClosure;

    public RegularExpressionBuilder(Graph<Instruction> graph) {
        this.graph = graph;
        this.transitiveClosure = TransitiveClosure.forGraph(graph);
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public RegexNode evaluateExpression(final GraphNode graphNode,
                                        final RegexNode expression,
                                        final GraphNode parallelBranch,
                                        final GraphNode[] sinkBranch) {
        return expression.accept(new RegularExpressionVisitor<RegexNode>() {
            @Override
            public RegexNode visitVariable(RegexVariable variable) {
                return trackVariable(graphNode, variable, parallelBranch, sinkBranch);
            }

            @Override
            public RegexNode visitConcatenation(RegexConcatenation concatenation) {
                RegexConcatenation newConcatenation = new RegexConcatenation();
                for (RegexNode childNode : concatenation.getChildNodes()) {
                    newConcatenation.connectNode(evaluateExpression(graphNode, childNode, parallelBranch, sinkBranch));
                }
                return newConcatenation;
            }

            @Override
            public RegexNode visitAlternation(RegexAlternation alternation) {
                RegexAlternation newAlternation = new RegexAlternation();
                for (RegexNode childNode : alternation.getChildNodes()) {
                    newAlternation.connectNode(evaluateExpression(graphNode, childNode, parallelBranch, sinkBranch));
                }
                return newAlternation;
            }

            @Override
            public RegexNode visitStar(RegexStar star) {
                return new RegexStar(evaluateExpression(graphNode, star.getChildNode(), parallelBranch, sinkBranch));
            }

            @Override
            public RegexNode visitPlus(RegexPlus plus) {
                return new RegexPlus(evaluateExpression(graphNode, plus.getChildNode(), parallelBranch, sinkBranch));
            }

            @Override
            public RegexNode visitAnyNode(RegexNode node) {
                return node;
            }
        });
    }

    public RegexNode trackVariable(final GraphNode graphNode,
                                   final RegexVariable variable,
                                   final GraphNode parallelBranch,
                                   final GraphNode[] sinkBranch) {
        Set<GraphNode> nodes = graph.getOutNodes(graphNode);
        assert nodes.size() > 0 && nodes.size() <= 2;
        if (nodes.size() > 1) {
            Iterator<GraphNode> it = nodes.iterator();
            GraphNode first = it.next();
            GraphNode second = it.next();

            RegexNode result;

            GraphNode[] sinkBranch1 = {null};
            GraphNode[] sinkBranch2 = {null};
            RegexNode leftNode = evaluateVariable(first, variable, second, sinkBranch1);
            RegexNode rightNode = evaluateVariable(second, variable, first, sinkBranch2);

            Tuple<RegexVariable, RegexNode> leftNodeVariable = getLeftmostVariable(leftNode, false);
            Tuple<RegexVariable, RegexNode> rightNodeVariable = getLeftmostVariable(rightNode, false);
            if (leftNodeVariable.getFirst() != null && leftNodeVariable.getFirst() == rightNodeVariable.getFirst()) {
                result = new RegexConcatenation(leftNodeVariable.getFirst(),
                        new RegexAlternation(leftNodeVariable.getSecond(), rightNodeVariable.getSecond()));
            } else {
                result = new RegexAlternation(leftNode, rightNode);
            }

            if (sinkBranch1[0] != null) {
                return evaluateExpression(sinkBranch1[0], result, parallelBranch, sinkBranch);
            }
            if (sinkBranch2[0] != null) {
                return evaluateExpression(sinkBranch2[0], result, parallelBranch, sinkBranch);
            }
            return result;
        }
        return evaluateVariable(nodes.iterator().next(), variable, parallelBranch, sinkBranch);
    }

    public RegexNode evaluateVariable(final GraphNode graphNode,
                                      final RegexVariable variable,
                                      final GraphNode parallelBranch,
                                      final GraphNode[] sinkBranch) {
        final GraphNode[] currentNode = {graphNode};

        if (parallelBranch != null && transitiveClosure.hasPath(parallelBranch, currentNode[0])) {
            sinkBranch[0] = currentNode[0];
            return variable;
        }

        RegexNode result = graph.getValue(currentNode[0]).accept(new InstructionVisitor<RegexNode>() {
            @Override
            public RegexNode visitAssignmentInstruction(AssignmentInstruction instruction) {
                if (instruction.getVariable() == variable) {
                    // TODO: add to cache
                    return evaluateExpression(currentNode[0], instruction.getExpression(), parallelBranch, sinkBranch);
                } else {
                    return null;
                }
            }

            @Override
            public RegexNode visitInstruction(Instruction instruction) {
                return null;
            }
        });
        if (result != null) {
            return result;
        } else {
            return trackVariable(graphNode, variable, parallelBranch, sinkBranch);
        }
    }

    public RegexNode processGraphNode(final GraphNode node) {
        System.out.println("Regular expression for node: " + graph.getValue(node));

        return graph.getValue(node).accept(new InstructionVisitor<RegexNode>() {
            @Override
            public RegexNode visitAssignmentInstruction(AssignmentInstruction instruction) {
                GraphNode[] sinkBranch = {null};
                return evaluateExpression(node, instruction.getExpression(), null, sinkBranch);
            }

            @Override
            public RegexNode visitInstruction(Instruction instruction) {
                throw new RuntimeException("Node isn't representing assignment");
            }
        });
    }

    public static RegexNode simplifyRegularExpression(RegexNode original) {
        return original.accept(new RegularExpressionVisitor<RegexNode>() {
            @Override
            public RegexNode visitConcatenation(RegexConcatenation concatenation) {
                RegexConcatenation newConcatenation = new RegexConcatenation();
                for (RegexNode childNode : concatenation.getChildNodes()) {
                    RegexNode node = simplifyRegularExpression(childNode);
                    if (node != null) {
                        newConcatenation.connectNode(node);
                    }
                }
                List<RegexNode> childNodes = newConcatenation.getChildNodes();
                if (childNodes.isEmpty()) {
                    return null;
                }
                if (childNodes.size() == 1) {
                    return childNodes.get(0);
                }
                return newConcatenation;
            }

            @Override
            public RegexNode visitAlternation(RegexAlternation alternation) {
                RegexAlternation newAlternation = new RegexAlternation();
                for (RegexNode childNode : alternation.getChildNodes()) {
                    RegexNode node = simplifyRegularExpression(childNode);
                    if (node != null) {
                        newAlternation.connectNode(node);
                    }
                }
                List<RegexNode> childNodes = newAlternation.getChildNodes();
                if (childNodes.isEmpty()) {
                    return null;
                }
                if (childNodes.size() == 1) {
                    return childNodes.get(0);
                }
                return newAlternation;
            }

            @Override
            public RegexNode visitEmpty(RegexEmpty empty) {
                return null;
            }

            @Override
            public RegexNode visitStar(RegexStar star) {
                return simplifyRegularExpression(star.getChildNode());
            }

            @Override
            public RegexNode visitPlus(RegexPlus plus) {
                return simplifyRegularExpression(plus.getChildNode());
            }

            @Override
            public RegexNode visitAnyNode(RegexNode node) {
                return node;
            }
        });
    }

    public static Tuple<RegexVariable, RegexNode> getLeftmostVariable(RegexNode expression, final boolean found) {
        return expression.accept(new RegularExpressionVisitor<Tuple<RegexVariable, RegexNode>>() {
            @Override
            public Tuple<RegexVariable, RegexNode> visitConcatenation(RegexConcatenation concatenation) {
                ListIterator<RegexNode> it = concatenation.getChildNodes().listIterator();
                Tuple<RegexVariable, RegexNode> result = new Tuple<RegexVariable, RegexNode>(null, null);
                if (it.hasNext()) {
                    result = getLeftmostVariable(it.next(), false);
                }
                if (result.getFirst() == null) {
                    return new Tuple<RegexVariable, RegexNode>(null, concatenation);
                }
                RegexConcatenation newConcatenation = new RegexConcatenation();
                if (result.getSecond() != null) {
                    newConcatenation.connectNode(result.getSecond());
                }
                while (it.hasNext()) {
                    newConcatenation.connectNode(it.next());
                }
                return new Tuple<RegexVariable, RegexNode>(result.getFirst(), newConcatenation);
            }

            @Override
            public Tuple<RegexVariable, RegexNode> visitVariable(RegexVariable variable) {
                if (!found) {
                    return new Tuple<RegexVariable, RegexNode>(variable, null);
                } else {
                    return visitAnyNode(variable);
                }
            }

            @Override
            public Tuple<RegexVariable, RegexNode> visitAnyNode(RegexNode node) {
                return new Tuple<RegexVariable, RegexNode>(null, node);
            }
        });
    }
}
