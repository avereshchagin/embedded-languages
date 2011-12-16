package com.github.avereshchagin.emblang.regex;

import com.github.avereshchagin.emblang.graph.*;

import java.util.*;

public class RegularExpressionBuilder {

    private Set<RegexVariable> variables;
    private final Map<RegexVariable, RegexNode> values = new HashMap<RegexVariable, RegexNode>();

    private RegexNode replaceVariables(final RegexNode node, final Map<RegexVariable, RegexNode> values) {
        return node.accept(new RegularExpressionVisitor<RegexNode>() {
            @Override
            public RegexNode visitVariable(RegexVariable variable) {
                if (values.containsKey(variable)) {
                    return values.get(variable);
                }
                return visitAnyNode(variable);
            }

            @Override
            public RegexNode visitConcatenation(RegexConcatenation concatenation) {
                RegexConcatenation newConcatenation = new RegexConcatenation();
                for (RegexNode childNode : concatenation.childNodes) {
                    newConcatenation.connectNode(replaceVariables(childNode, values));
                }
                return newConcatenation;
            }

            @Override
            public RegexNode visitAlternation(RegexAlternation alternation) {
                RegexConcatenation newAlternation = new RegexConcatenation();
                for (RegexNode childNode : alternation.childNodes) {
                    newAlternation.connectNode(replaceVariables(childNode, values));
                }
                return newAlternation;
            }

            @Override
            public RegexNode visitStar(RegexStar star) {
                return new RegexStar(replaceVariables(star, values));
            }

            @Override
            public RegexNode visitPlus(RegexPlus plus) {
                return new RegexPlus(replaceVariables(plus, values));
            }

            @Override
            public RegexNode visitAnyNode(RegexNode node) {
                return node;
            }
        });
    }

    private Set<RegexVariable> findUsedVariables(List<Node<NodeData>> topOrdering) {
        Set<RegexVariable> variables = new HashSet<RegexVariable>();
        for (Node<NodeData> node : topOrdering) {
            RegexVariable variable = node.getData().getVariable();
            if (variable != null) {
                variables.add(variable);
            }
        }
        return variables;
    }

//    private CfgStatement buildByLoopControlFlow(CfgStatement start, int loopId) {
//        List<CfgAssignmentStatement> preLoopNodes = new ArrayList<CfgAssignmentStatement>();
//        while (true) {
//            if (start instanceof CfgAssignmentStatement) {
//                preLoopNodes.add((CfgAssignmentStatement) start);
//            }
//            if (start.getOutgoingEdges().size() > 1) {
//                break;
//            }
//            start = start.getOutgoingEdges().get(0).getDestination();
//        }
//
//        CfgStatement out = start;
//        List<CfgAssignmentStatement> loopNodes = new ArrayList<CfgAssignmentStatement>();
//        do {
//            for (CfgEdge edge : start.getOutgoingEdges()) {
//                if (edge.getDestination().containsLoop(loopId)) {
//                    start = edge.getDestination();
//                }
//            }
//            if (start instanceof CfgAssignmentStatement) {
//                loopNodes.add((CfgAssignmentStatement) start);
//            }
//        } while (start != out);
//
//        Map<RegexVariable, RegexNode> loopValues = new HashMap<RegexVariable, RegexNode>();
//        for (RegexVariable variable : variables) {
//            loopValues.put(variable, new RegexEmpty());
//        }
//        for (CfgAssignmentStatement node : loopNodes) {
//            if (variables.contains(node.getVariable())) {
//                loopValues.put(node.getVariable(),
//                        replaceVariables(node.getExpression(), loopValues));
//            }
//        }
//
//        for (RegexVariable variable : variables) {
//            values.put(variable, new RegexConcatenation(values.get(variable), new RegexStar(loopValues.get(variable))));
//        }
//
//        for (CfgEdge edge : out.getOutgoingEdges()) {
//            if (!edge.getDestination().containsLoop(loopId)) {
//                return edge.getDestination();
//            }
//        }
//        return out;
//    }

    private Map<RegexVariable, RegexNode> initDefaultValues() {
        Map<RegexVariable, RegexNode> values = new HashMap<RegexVariable, RegexNode>();
        for (RegexVariable variable : variables) {
            values.put(variable, variable);
        }
        return values;
    }

    private void buildByLinearControlFlow(final Node<NodeData> source, final Node<NodeData> sink,
                                          Map<RegexVariable, RegexNode> values) {
        Node<NodeData> node = source;
        while (true) {
            RegexVariable variable = node.getData().getVariable();
            if (variable != null) {
                values.put(variable, replaceVariables(node.getData().getExpression(), values));
            }
            if (node == sink) {
                break;
            }
            Fork<NodeData> fork = node.getOutgoingFork();
            if (fork != null) {
                Map<RegexVariable, RegexNode> val1 = initDefaultValues();
                buildByLinearControlFlow(node.getOutgoingEdges().get(0).getDestination(), fork.getSink(), val1);
                Map<RegexVariable, RegexNode> val2 = initDefaultValues();
                buildByLinearControlFlow(node.getOutgoingEdges().get(1).getDestination(), fork.getSink(), val2);
                for (RegexVariable var : variables) {
                    values.put(var, replaceVariables(new RegexAlternation(val1.get(var), val2.get(var)), values));
                }
                node = fork.getSink();
            }
            List<Edge<NodeData>> edges = node.getOutgoingEdges();
            if (edges.isEmpty()) {
                break;
            }
            node = edges.get(0).getDestination();
        }
    }

    public RegexNode buildRegularExpression(Graph<NodeData> graph, Node<NodeData> node) {
        ReversedDFS<NodeData> dfs = new ReversedDFS<NodeData>(graph, Collections.singletonList(node));
        dfs.findLoops();
        Warshall<NodeData> warshall = new Warshall<NodeData>(graph);
        warshall.findForks();
        variables = findUsedVariables(dfs.getTopOrdering());

        for (RegexVariable variable : variables) {
            values.put(variable, new RegexEmpty());
        }
        buildByLinearControlFlow(dfs.getTopOrdering().get(0), node, values);

        return values.get(node.getData().getVariable());
    }

    public void log(String message) {
        System.out.println(message);
    }
}
