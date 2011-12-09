package com.github.avereshchagin.emblang.regex;

import com.github.avereshchagin.emblang.cfg.CfgAssignmentStatement;
import com.github.avereshchagin.emblang.cfg.CfgEdge;
import com.github.avereshchagin.emblang.cfg.CfgStatement;

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

    private Set<RegexVariable> findUsedVariables(CfgAssignmentStatement statement, List<CfgStatement> topOrdering) {
        Set<RegexVariable> variables = new HashSet<RegexVariable>();
        int index = topOrdering.indexOf(statement);
        if (index >= 0) {
            variables.add(statement.getVariable());
            variables.addAll(statement.getExpression().findUsedVariables());
            ListIterator<CfgStatement> it = topOrdering.listIterator(index);
            while (it.hasPrevious()) {
                CfgStatement previous = it.previous();
                if (previous instanceof CfgAssignmentStatement) {
                    CfgAssignmentStatement assignmentStatement = (CfgAssignmentStatement) previous;
                    if (variables.contains(assignmentStatement.getVariable())) {
                        variables.addAll(assignmentStatement.getExpression().findUsedVariables());
                    }
                }
            }
        }
        return variables;
    }

    private CfgStatement buildByLoopControlFlow(CfgStatement start, int loopId) {
        List<CfgAssignmentStatement> preLoopNodes = new ArrayList<CfgAssignmentStatement>();
        while (true) {
            if (start instanceof CfgAssignmentStatement) {
                preLoopNodes.add((CfgAssignmentStatement) start);
            }
            if (start.getOutgoingEdges().size() > 1) {
                break;
            }
            start = start.getOutgoingEdges().get(0).getDestination();
        }

        CfgStatement out = start;
        List<CfgAssignmentStatement> loopNodes = new ArrayList<CfgAssignmentStatement>();
        do {
            for (CfgEdge edge : start.getOutgoingEdges()) {
                if (edge.getDestination().containsLoop(loopId)) {
                    start = edge.getDestination();
                }
            }
            if (start instanceof CfgAssignmentStatement) {
                loopNodes.add((CfgAssignmentStatement) start);
            }
        } while (start != out);

        Map<RegexVariable, RegexNode> loopValues = new HashMap<RegexVariable, RegexNode>();
        for (RegexVariable variable : variables) {
            loopValues.put(variable, new RegexEmpty());
        }
        for (CfgAssignmentStatement node : loopNodes) {
            if (variables.contains(node.getVariable())) {
                loopValues.put(node.getVariable(),
                        replaceVariables(node.getExpression(), loopValues));
            }
        }

        for (RegexVariable variable : variables) {
            values.put(variable, new RegexConcatenation(values.get(variable), new RegexStar(loopValues.get(variable))));
        }

        for (CfgEdge edge : out.getOutgoingEdges()) {
            if (!edge.getDestination().containsLoop(loopId)) {
                return edge.getDestination();
            }
        }
        return out;
    }

    private RegexNode buildByLinearControlFlow(CfgStatement start, CfgStatement end, RegexVariable variable) {
        CfgStatement current = start;
        while (true) {
            if (current instanceof CfgAssignmentStatement) {
                CfgAssignmentStatement currentAssignment = (CfgAssignmentStatement) current;
                if (variables.contains(currentAssignment.getVariable())) {
                    values.put(currentAssignment.getVariable(),
                            replaceVariables(currentAssignment.getExpression(), values));
                }
            }
            if (current == end) {
                break;
            }
            if (current.hasLoop()) {
                current = buildByLoopControlFlow(current, current.getLoopId());
            } else {
                current = current.getOutgoingEdges().get(0).getDestination();
            }
        }
        return values.get(variable);
    }

    public RegexNode buildRegularExpression(CfgStatement start, CfgStatement statement, List<CfgStatement> topOrdering) {
        if (statement instanceof CfgAssignmentStatement) {
            variables = findUsedVariables((CfgAssignmentStatement) statement, topOrdering);
            for (RegexVariable variable : variables) {
                values.put(variable, new RegexEmpty());
            }
            return buildByLinearControlFlow(start, statement, ((CfgAssignmentStatement) statement).getVariable());
        }
        return new RegexEmpty();
    }

    public void log(String message) {
//        System.out.println(message);
    }
}
