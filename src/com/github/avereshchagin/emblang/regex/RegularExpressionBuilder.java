package com.github.avereshchagin.emblang.regex;

import com.github.avereshchagin.emblang.cfg.CfgAssignmentStatement;
import com.github.avereshchagin.emblang.cfg.CfgStatement;

import java.util.*;

public class RegularExpressionBuilder {

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

    public RegexNode buildRegularExpression(CfgStatement statement, List<CfgStatement> topOrdering) {
        if (statement instanceof CfgAssignmentStatement) {
            Set<RegexVariable> variables = findUsedVariables((CfgAssignmentStatement) statement, topOrdering);
            Map<RegexVariable, RegexNode> values = new HashMap<RegexVariable, RegexNode>();
            for (RegexVariable variable : variables) {
                values.put(variable, new RegexEmpty());
            }
            for (CfgStatement node : topOrdering) {
                if (node instanceof CfgAssignmentStatement) {
                    CfgAssignmentStatement assignmentStatement = (CfgAssignmentStatement) node;
                    if (variables.contains(assignmentStatement.getVariable())) {
                        values.put(assignmentStatement.getVariable(), replaceVariables(assignmentStatement.getExpression(), values));
                    }
                    if (node == statement) {
                        return values.get(((CfgAssignmentStatement) statement).getVariable());
                    }
                }
            }
        }
        return new RegexEmpty();
    }

    public void log(String message) {
//        System.out.println(message);
    }
}
