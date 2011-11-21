package com.github.avereshchagin.emblang.regex;

import com.github.avereshchagin.emblang.cfg.CfgEdge;
import com.github.avereshchagin.emblang.cfg.CfgStatement;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RegularExpressionBuilder {

    public static RegexNode processRegexNode(final RegexNode node, final CfgStatement borderStatement, final CfgStatement startStatement) {
        log("processRegexNode");
        return node.accept(new RegularExpressionVisitor<RegexNode>() {

            @Override
            public RegexNode visitConcatenation(RegexConcatenation concatenation) {
                log("Concatenation: " + concatenation);
                List<RegexNode> childNodes = concatenation.getChildNodes();
                Iterator<RegexNode> iterator = childNodes.iterator();
                if (iterator.hasNext()) {
                    RegexNode currentNode = processRegexNode(iterator.next(), borderStatement, startStatement);
                    while (iterator.hasNext()) {
                        // TODO: rewrite as .connectNode
                        currentNode = new RegexConcatenation(currentNode, processRegexNode(iterator.next(), borderStatement, startStatement));
                    }
                    return currentNode;
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitLiteral(RegexLiteral literal) {
                log("Literal: " + literal);
                return literal;
            }

            @Override
            public RegexNode visitVariable(RegexVariable variable) {
                log("Variable: " + variable);
                return fork(startStatement, borderStatement, variable);
            }

            @Override
            public RegexNode visitAssignment(RegexAssignment assignment) {
                log("Assignment: " + assignment);
                return processRegexNode(assignment.getChildNode(), borderStatement, startStatement);
            }

            @Override
            public RegexNode visitAlternation(RegexAlternation alternation) {
                log("Alternation: " + alternation);
                List<RegexNode> childNodes = alternation.getChildNodes();
                Iterator<RegexNode> iterator = childNodes.iterator();
                if (iterator.hasNext()) {
                    RegexNode currentNode = processRegexNode(iterator.next(), borderStatement, startStatement);
                    while (iterator.hasNext()) {
                        // TODO: rewrite as .connectNode
                        currentNode = new RegexAlternation(currentNode, processRegexNode(iterator.next(), borderStatement, startStatement));
                    }
                    return currentNode;
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitEmpty(RegexEmpty empty) {
                return empty;
            }

            @Override
            public RegexNode visitRange(RegexRange range) {
                return range;
            }

            @Override
            public RegexNode visitExpression(RegexExpression expression) {
                log("Expression: " + expression);
                ListIterator<RegexNode> iterator = expression.getChildNodes().listIterator(expression.getChildNodes().size());
                if (iterator.hasPrevious()) {
                    return processRegexNode(iterator.previous(), borderStatement, startStatement);
                }
                return new RegexEmpty();
            }

            @Override
            public RegexNode visitStar(RegexStar star) {
                return star;
            }

            @Override
            public RegexNode visitPlus(RegexPlus plus) {
                return plus;
            }

            @Override
            public RegexNode visitAnyNode(RegexNode node) {
                log("Unsupported node: " + node);
                return new RegexEmpty();
            }
        });
    }

    public static RegexNode processStatement(CfgStatement statement, CfgStatement borderStatement, RegexVariable variable) {
        log("Process statement: " + statement);
        RegexAssignment assignment = statement.getAssignment(variable);
        if (assignment != null) {
            return processRegexNode(assignment, borderStatement, statement);
        }
        if (statement != borderStatement) {
            return fork(statement, borderStatement, variable);
        }
        return new RegexEmpty();
    }

    public static RegexNode fork(CfgStatement statement, CfgStatement borderStatement, RegexVariable variable) {
        log("Fork");
        for (CfgEdge edge : statement.getOutgoingEdges()) {
            if (edge.getType() != null) {
                if (edge.getType().equals(CfgEdge.Type.BACK)) {
                    if (statement.getIncomingEdges().size() > 0) {
                        if (edge.getDestination().isAtLeastOnce()) {
                            return new RegexPlus(processStatement(
                                    statement.getIncomingEdges().get(0).getSource(), edge.getDestination(), variable));
                        } else {
                            return new RegexStar(processStatement(
                                    statement.getIncomingEdges().get(0).getSource(), edge.getDestination(), variable));
                        }
                    }
                }
            }
        }
        // TODO: check if has virtual edge
        if (statement.getIncomingEdges().size() > 0) {
            return processStatement(statement.getIncomingEdges().get(0).getSource(), borderStatement, variable);
        }
        return new RegexEmpty();
    }

    public static RegexNode buildRegularExpression(CfgStatement statement) {
        return processRegexNode(statement.getRegexExpression(), null, statement);
    }

    public static void log(String message) {
//        System.out.println(message);
    }
}
